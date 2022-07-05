import { IotData } from "aws-sdk";

export class IoTService {
  private iotData: IotData;

  constructor(private topic: string) {
    this.iotData = new IotData({
      endpoint: process.env.AWS_IOT_ENDPOINT,
      region: process.env.AWS_ACCOUNT_REGION
    });
  }

  async publishMessage(message: Object): Promise<void> {
    const params = {
      topic: this.topic,
      payload: JSON.stringify(message),
      qos: 0
    };
    await this.iotData.publish(params).promise();
  }
}

// const ioTService = new IoTService();

// ioTService.publishMessage("test", { test: "Hello World" });
