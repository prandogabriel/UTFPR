/* esp-idf-telegram-bot
 *
 * Author: antusystem
 * e-mail: aleantunes95@gmail.com
 * Date: 11-01-2020
 * MIT License
 * As it is described in the readme file
 *
 */

#include <string.h>
#include <stdlib.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "esp_log.h"
#include "esp_system.h"
#include "nvs_flash.h"
#include "esp_event.h"
#include "esp_netif.h"
#include "esp_tls.h"

#include "cJSON.h"

#include "lwip/err.h"
#include "lwip/sys.h"
#include "esp_wifi.h"
#include "esp_http_client.h"
#include "driver/gpio.h"
#include "freertos/event_groups.h"

/*HTTP VARS*/
#define API_BASE_URL "https://eq62sd0kbj.execute-api.us-east-1.amazonaws.com"
#define API_PATH_NEW_ACCESS "/prd/new-access"

/*HTTP buffer*/
#define MAX_HTTP_RECV_BUFFER 1024
#define MAX_HTTP_OUTPUT_BUFFER 2048

/* TAGs for the system*/
static const char *HTTP_TAG = "HTTP_CLIENT Handler";
static const char *POST_TAG = "send pic";

char url_string[512] = API_BASE_URL;

/* Root cert generate with script certificado.sh:
 *
   To embed it in the app binary, the PEM file is named
   in the component.mk COMPONENT_EMBED_TXTFILES variable.
*/
extern const char client01_pem_start[] asm("_binary_client01_pem_start");
extern const char client01_pem_end[] asm("_binary_client01_pem_end");

extern const char client01_key_start[] asm("_binary_client01_key_start");
extern const char client01_key_end[] asm("_binary_client01_key_end");

esp_err_t _http_event_handler(esp_http_client_event_t *evt)
{
  static char *output_buffer; // Buffer to store response of http request from event handler
  static int output_len;      // Stores number of bytes read
  switch (evt->event_id)
  {
  case HTTP_EVENT_ERROR:
    ESP_LOGD(HTTP_TAG, "HTTP_EVENT_ERROR");
    break;
  case HTTP_EVENT_ON_CONNECTED:
    ESP_LOGD(HTTP_TAG, "HTTP_EVENT_ON_CONNECTED");
    break;
  case HTTP_EVENT_HEADER_SENT:
    ESP_LOGD(HTTP_TAG, "HTTP_EVENT_HEADER_SENT");
    break;
  case HTTP_EVENT_ON_HEADER:
    ESP_LOGD(HTTP_TAG, "HTTP_EVENT_ON_HEADER, key=%s, value=%s", evt->header_key, evt->header_value);
    break;
  case HTTP_EVENT_ON_DATA:
    ESP_LOGD(HTTP_TAG, "HTTP_EVENT_ON_DATA, len=%d", evt->data_len);
    /*
     *  Check for chunked encoding is added as the URL for chunked encoding used in this example returns binary data.
     *  However, event handler can also be used in case chunked encoding is used.
     */
    if (!esp_http_client_is_chunked_response(evt->client))
    {
      // If user_data buffer is configured, copy the response into the buffer
      if (evt->user_data)
      {
        memcpy(evt->user_data + output_len, evt->data, evt->data_len);
      }
      else
      {
        if (output_buffer == NULL)
        {
          output_buffer = (char *)malloc(esp_http_client_get_content_length(evt->client));
          output_len = 0;
          if (output_buffer == NULL)
          {
            ESP_LOGE(HTTP_TAG, "Failed to allocate memory for output buffer");
            return ESP_FAIL;
          }
        }
        memcpy(output_buffer + output_len, evt->data, evt->data_len);
      }
      output_len += evt->data_len;
    }

    break;
  case HTTP_EVENT_ON_FINISH:
    ESP_LOGD(HTTP_TAG, "HTTP_EVENT_ON_FINISH");
    if (output_buffer != NULL)
    {
      // Response is accumulated in output_buffer. Uncomment the below line to print the accumulated response
      // ESP_LOG_BUFFER_HEX(HTTP_TAG, output_buffer, output_len);
      free(output_buffer);
      output_buffer = NULL;
    }
    output_len = 0;
    break;
  case HTTP_EVENT_DISCONNECTED:
    ESP_LOGI(HTTP_TAG, "HTTP_EVENT_DISCONNECTED");
    int mbedtls_err = 0;
    esp_err_t err = esp_tls_get_and_clear_last_error(evt->data, &mbedtls_err, NULL);
    if (err != 0)
    {
      if (output_buffer != NULL)
      {
        free(output_buffer);
        output_buffer = NULL;
      }
      output_len = 0;
      ESP_LOGI(HTTP_TAG, "Last esp error code: 0x%x", err);
      ESP_LOGI(HTTP_TAG, "Last mbedtls failure: 0x%x", mbedtls_err);
    }
    break;
  }
  return ESP_OK;
}

static void https_perform_post(uint8_t *buf)
{
  char url[512] = "";

  char output_buffer[MAX_HTTP_OUTPUT_BUFFER] = {0}; // Buffer to store response of http request
  esp_http_client_config_t config = {
      .url = API_BASE_URL,
      .transport_type = HTTP_TRANSPORT_OVER_SSL,
      .event_handler = _http_event_handler,
      .client_cert_pem = client01_pem_start,
      .client_key_pem = client01_key_start,
      .user_data = output_buffer,
  };
  // POST
  ESP_LOGW(POST_TAG, "início requisição");
  esp_http_client_handle_t client = esp_http_client_init(&config);

  /* Creating the string of the url*/
  strcat(url, url_string);
  // Passing the method
  strcat(url, API_PATH_NEW_ACCESS);
  // ESP_LOGW(POST_TAG, "url string es: %s",url);
  // You set the real url for the request
  esp_http_client_set_url(client, url);

  ESP_LOGW(POST_TAG, "Enviando POST");

  // gerando body json para API
  cJSON *body;
  char *out;

  body = cJSON_CreateObject();

  cJSON_AddItemToObject(body, "fileEncoded", cJSON_CreateString((const char *)buf));

  out = cJSON_Print(body);

  esp_http_client_set_method(client, HTTP_METHOD_POST);
  esp_http_client_set_header(client, "Content-Type", "application/json");
  esp_http_client_set_post_field(client, (const char *)out, strlen((const char *)out));

  esp_err_t err = esp_http_client_perform(client);
  if (err == ESP_OK)
  {
    ESP_LOGI(POST_TAG, "HTTP POST Status = %d, content_length = %d",
             esp_http_client_get_status_code(client),
             esp_http_client_get_content_length(client));
    ESP_LOGW(POST_TAG, "Desde Perform el output es: %s", output_buffer);
  }
  else
  {
    ESP_LOGE(POST_TAG, "HTTP POST request failed: %s", esp_err_to_name(err));
  }

  ESP_LOGW(HTTP_TAG, "clear");
  esp_http_client_close(client);
  esp_http_client_cleanup(client);
  ESP_LOGI(POST_TAG, "esp_get_free_heap_size: %d", esp_get_free_heap_size());
}

static void http_post(uint8_t *buf)
{
  ESP_LOGW(HTTP_TAG, "Wait 1/2 second before start");
  vTaskDelay(500 / portTICK_PERIOD_MS);

  ESP_LOGW(HTTP_TAG, "https_perform_post");
  https_perform_post(buf);

  ESP_LOGI(HTTP_TAG, "Finish http example");
  vTaskDelete(NULL);
}
