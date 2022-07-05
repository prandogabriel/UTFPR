import { SNS } from "aws-sdk";
import { PublishInput } from "aws-sdk/clients/sns";

export class SnsService<T> {
  protected readonly sns: SNS;

  private baseUrl: string;

  constructor(private topic: string) {
    this.sns = new SNS();
    this.baseUrl = `arn:aws:sns:${process.env.AWS_ACCOUNT_REGION}:${process.env.AWS_ACCOUNT_ID}`;
  }

  publish(message: T) {
    const param = {
      TopicArn: this.getTopicArn,
      Message: JSON.stringify(message)
    } as PublishInput;

    return this.sns.publish(param).promise();
  }

  protected get getTopicArn(): string {
    return `${this.baseUrl}:${this.topic}`;
  }
}
