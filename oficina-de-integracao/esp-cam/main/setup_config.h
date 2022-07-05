#include <nvs_flash.h>
#include <sys/param.h>
#include "esp_log.h"

void setup_init(const char *TAG_LOGI)
{
  ESP_LOGI(TAG_LOGI, "Hello, Starting up!");

  esp_err_t ret = nvs_flash_init();
  if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
  {
    ESP_ERROR_CHECK(nvs_flash_erase());
    ret = nvs_flash_init();
  }
  ESP_ERROR_CHECK(ret);
}
