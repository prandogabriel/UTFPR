
#define BUTTON_SIDE_PIN 15
// support IDF 5.x
#ifndef portTICK_RATE_MS
#define portTICK_RATE_MS portTICK_PERIOD_MS
#endif

//#define BLINK_LED 2

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/queue.h"
#include "driver/gpio.h"

#include "cam.h"
#include "request.h"
#include "custom_base64.h"

#define GPIO_INPUT_IO_0     15
#define GPIO_INPUT_PIN_SEL  ((1ULL<<GPIO_INPUT_IO_0))
#define ESP_INTR_FLAG_DEFAULT 0
#define EVER ;;

static xQueueHandle gpio_evt_queue = NULL;

static void IRAM_ATTR gpio_isr_handler(void* arg)
{
    uint32_t gpio_num = (uint32_t) arg;
    gpio_set_intr_type(GPIO_INPUT_IO_0, GPIO_INTR_DISABLE);

    xQueueSendFromISR(gpio_evt_queue, &gpio_num, NULL);
}

static void gpio_task_example(void* arg)
{
    uint32_t io_num;
    //printf("Chegamos no handler");
    for(EVER) {
        if(xQueueReceive(gpio_evt_queue, &io_num, portMAX_DELAY)) {
            vTaskDelay(50);

            // tirar foto
            ESP_LOGI(TAG_CAM, "Taking picture...");
            camera_fb_t *pic = esp_camera_fb_get();

            // use pic->buf to access the image
            ESP_LOGI(TAG_CAM, "Picture taken! Its size was: %zu bytes", pic->len);
            esp_camera_fb_return(pic);

            uint8_t *base_64 = get_base64(pic);

            // fazer requisição http para o servidor
            // http_post(pic->buf);
            http_post(base_64);

            printf("GPIO[%d] intr, val: %d\n", io_num, gpio_get_level(io_num));
            gpio_set_intr_type(GPIO_INPUT_IO_0, GPIO_INTR_NEGEDGE);

        }
    }
}
