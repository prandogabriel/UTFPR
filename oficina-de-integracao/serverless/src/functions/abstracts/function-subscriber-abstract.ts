import { Context, SNSEvent } from "aws-lambda";

export abstract class FunctionSubscriberAbstract<MESSAGE> {
  protected abstract buildRequest(message: string): MESSAGE;

  protected abstract execute(request: MESSAGE): Promise<void>;

  protected abstract onErrorOnMessage(error: Error, message: MESSAGE): Promise<void>;

  async run(event: SNSEvent, context: Context): Promise<void> {
    context.callbackWaitsForEmptyEventLoop = false;
    try {
      console.log("event: ", event.Records);
      console.log(context.functionName);
      await Promise.all(
        event.Records.map(async (record) => {
          const builtMessage = this.buildRequest(record.Sns.Message);
          try {
            return await this.execute(builtMessage);
          } catch (e) {
            return this.onErrorOnMessage(e as Error, builtMessage);
          }
        })
      );
    } catch (error) {
      console.log("", error);
      throw error;
    }
  }
}
