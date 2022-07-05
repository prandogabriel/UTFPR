/*
cd /PATH_to_PROJECT/codes/doorlock/
. $HOME/esp/esp-idf/export.sh
idf.py build
idf.py -p /dev/ttyUSBx flash monitor
*/
//Remover libs
#include <stdio.h>
#include "driver/gpio.h"
#include <string.h>
#include "freertos/FreeRTOS.h"
#include "freertos/timers.h"
#include "freertos/event_groups.h"
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"

//#include "wifi.h"
#include "button.h"
#include "setup_config.h"
#include "wpa2.h"
// #include "mqtt.h"

static const char *TAG_LOGI = "doorlock";

void app_main(void)
{
  // Initialize NVS
  setup_init(TAG_LOGI);

  //wifi_init_sta();
  //WPA2
  ESP_ERROR_CHECK( nvs_flash_init() );
  initialise_wifi();
  xTaskCreate(&wpa2_enterprise_example_task, "wpa2_enterprise_example_task", 4096, NULL, 5, NULL);
  // ESP_LOGI(TAG_WIFI, "wifi pulado");

  if (ESP_OK != init_camera())
  {
    return;//A funcao init_camera instala o gpio isr service
  }

  // Init GPIO TASKS
  // zero-initialize the config structure.
  gpio_config_t io_conf = {};

  // interrupt of rising edge
  io_conf.intr_type = GPIO_INTR_POSEDGE;
  // bit mask of the pins, use GPIO4/5 here
  io_conf.pin_bit_mask = GPIO_INPUT_PIN_SEL;
  // set as input mode
  io_conf.mode = GPIO_MODE_INPUT;
  // enable pull-up mode
  io_conf.pull_up_en = 1;
  gpio_config(&io_conf);

  // change gpio interrupt type for one pin
  gpio_set_intr_type(GPIO_INPUT_IO_0, GPIO_INTR_ANYEDGE);

  // create a queue to handle gpio event from isr
  gpio_evt_queue = xQueueCreate(10, sizeof(uint32_t));
  // start gpio task
  xTaskCreate(gpio_task_example, "gpio_task_example", 8192, NULL, 10, NULL);

  // hook isr handler for specific gpio pin
  gpio_isr_handler_add(GPIO_INPUT_IO_0, gpio_isr_handler, (void *)GPIO_INPUT_IO_0);

  printf("Minimum free heap size: %d bytes\n", esp_get_minimum_free_heap_size());

  //Init MQTT

  //Apenas LOG_DEBUG
  //ESP_LOGI(TAG, "[APP] Startup..");
  //ESP_LOGI(TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
  //ESP_LOGI(TAG, "[APP] IDF version: %s", esp_get_idf_version());

  // esp_log_level_set("*", ESP_LOG_INFO);
  // esp_log_level_set("esp-tls", ESP_LOG_VERBOSE);
  // esp_log_level_set("MQTT_CLIENT", ESP_LOG_VERBOSE);
  // esp_log_level_set("MQTT_EXAMPLE", ESP_LOG_VERBOSE);
  // esp_log_level_set("TRANSPORT_BASE", ESP_LOG_VERBOSE);
  // esp_log_level_set("TRANSPORT", ESP_LOG_VERBOSE);
  // esp_log_level_set("OUTBOX", ESP_LOG_VERBOSE);

  //ESP_ERROR_CHECK(nvs_flash_init()); Ja foi iniciado pela camera
  // ESP_ERROR_CHECK(esp_netif_init());
  // ESP_ERROR_CHECK(esp_event_loop_create_default());

  // mqtt_app_start();

  int i = 1;
  int cnt = 0;
  while (i == 1)
  {
    printf("cnt: %d\n", cnt++);
    vTaskDelay(5000 / portTICK_RATE_MS);
  }
}
