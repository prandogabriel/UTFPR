import base64
import cv2
import datetime
import json
import time
import requests

API_BASE_URL = "https://eq62sd0kbj.execute-api.us-east-1.amazonaws.com"
API_PATH_NEW_ACCESS = "/prd/new-access"
url = API_BASE_URL + API_PATH_NEW_ACCESS

cam = cv2.VideoCapture(0)

cv2.namedWindow("cam")

while True:
    ret, frame = cam.read()
    if not ret:
        print("failed to grab frame")
        break
    cv2.imshow("cam", frame)

    k = cv2.waitKey(1)
    if k%256 == 27:
        # ESC pressed
        print("Escape hit, closing...")
        break
    elif k%256 == 32:
        # SPACE pressed
        retval, buffer = cv2.imencode('.jpg', frame)
        image_encoded = (base64.b64encode(buffer))
        image_encoded = image_encoded.decode("utf-8")

        _params = {"fileEncoded": image_encoded}
        
        params_json_dump = json.dumps(_params)
        
        # Enviar foto para server
        response = requests.post(url, data=params_json_dump)
        # print(response.json())

cam.release()

cv2.destroyAllWindows()