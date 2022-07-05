#include <stdio.h>
#include <string.h>
#include "unity.h"
#include <mbedtls/base64.h>
#include "esp_log.h"

#include "esp_camera.h"

uint8_t * get_base64(const camera_fb_t *pic)
{
    uint8_t *outbuffer = NULL;
    size_t outsize = 0;
    if (PIXFORMAT_JPEG != pic->format) {
        fmt2jpg(pic->buf, pic->width * pic->height * 2, pic->width, pic->height, pic->format, 50, &outbuffer, &outsize);
    } else {
        outbuffer = pic->buf;
        outsize = pic->len;
    }

    uint8_t *base64_buf = calloc(1, outsize * 4);
    if (NULL != base64_buf) {
        size_t out_len = 0;
        mbedtls_base64_encode(base64_buf, outsize * 4, &out_len, outbuffer, outsize);
        // printf("%s\n", base64_buf);
        // return base64_buf;
        // free(base64_buf);
      // if (PIXFORMAT_JPEG != pic->format) {
      //     free(outbuffer);
      // }
    } else {
        // ESP_LOGE(TAG, "malloc for base64 buffer failed");
    }
    return base64_buf;
}
