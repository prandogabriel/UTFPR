import { S3 } from "aws-sdk";
import { GetObjectOutput, PutObjectOutput } from "aws-sdk/clients/s3";

export class S3Service {
  private s3: S3;

  constructor(protected bucketName: string) {
    const accessKeyId = process.env.ACCESS_KEY;
    const secretAccessKey = process.env.SECRET_ACCESS;

    this.s3 = new S3({ accessKeyId, secretAccessKey, region: "us-east-1", signatureVersion: "v4" });
  }

  async uploadImage(data: { id: string; file: string; contentType: string }): Promise<PutObjectOutput> {
    const requestS3 = {
      Bucket: this.bucketName,
      Key: data.id,
      Body: Buffer.from(data.file.replace(/^data:image\/\w+;base64,/, ""), "base64"),
      ContentEncoding: "base64",
      ContentType: data.contentType
    };

    return this.s3.putObject(requestS3).promise();
  }

  async verifyFile(id: string): Promise<boolean> {
    try {
      const requestS3: S3.Types.HeadObjectRequest = {
        Bucket: this.bucketName,
        Key: id
      };
      return !!(await this.s3.headObject(requestS3).promise());
    } catch (e) {
      console.log(e);
      return false;
    }
  }

  async findFile(id: string): Promise<GetObjectOutput> {
    const requestS3 = {
      Bucket: this.bucketName,
      Key: id
    };
    return this.s3.getObject(requestS3).promise();
  }

  async createSignedUrl(fileName: string, operation: string): Promise<{ url: string }> {
    const ONE_DAY_IN_SECONDS = 60 * 2 * 60 * 24;
    const signedUrlExpireSeconds = ONE_DAY_IN_SECONDS;

    const url = this.s3.getSignedUrl(operation, {
      Bucket: this.bucketName,
      Key: fileName,
      Expires: signedUrlExpireSeconds
    });

    return { url };
  }
}
