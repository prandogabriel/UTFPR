import { RekognitionClient, DetectCustomLabelsCommand } from "@aws-sdk/client-rekognition";

import { facesBucket } from "configuration";

export class RekognitionService {
  private client: RekognitionClient;

  constructor() {
    this.client = new RekognitionClient({});
  }

  async searchFace(name: string) {
    const command = new DetectCustomLabelsCommand({
      Image: { S3Object: { Bucket: facesBucket, Name: name } },
      ProjectVersionArn: process.env.REKOGNITION_COLLECTION_ID
    });

    console.log({ command });

    const response = await this.client.send(command);
    console.log({ response });

    return response.CustomLabels ? response.CustomLabels[0] : undefined;
  }
}
