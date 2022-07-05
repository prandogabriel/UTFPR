import { v4 } from "uuid";

import { S3Service } from "@services/s3-service";
import { SnsService } from "@services/sns-service";

import { facesBucket, photoTopic } from "../../configuration";

import { CustomResponse, HttpRequestWrapper } from "./@types";
import { FunctionAbstract } from "./abstracts/function-abstract";

import { noContent } from "@helpers/response";

type Request = {
  fileEncoded: string;
};

type Response = any;

export class NewAccessRequestFunction extends FunctionAbstract<Request, Response> {
  static instance: NewAccessRequestFunction;

  protected buildRequest(request: HttpRequestWrapper<string>): Request {
    return JSON.parse(request.body);
  }

  protected async execute({ fileEncoded }: Request): Promise<CustomResponse<Response>> {
    const fileKeyId = `${v4()}.jpeg`;
    const fileContentType = "image/jpeg";

    const s3 = new S3Service(facesBucket);
    const s3Result = await s3.uploadImage({ id: fileKeyId, file: fileEncoded, contentType: fileContentType });
    console.log({ s3Result });

    const sns = new SnsService(photoTopic);
    const snsPublishResult = await sns.publish({ fileKeyId, fileContentType });
    console.log({ snsPublishResult });

    return noContent();
  }

  static getInstance() {
    if (!this.instance) {
      this.instance = new this();
    }

    return this.instance;
  }
}
