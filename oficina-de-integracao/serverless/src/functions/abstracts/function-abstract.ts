import { Context } from "aws-lambda";

import { CustomResponse } from "@functions/@types";

import { internalServerError } from "../../helpers/response";

export abstract class FunctionAbstract<REQUEST, RESPONSE> {
  protected abstract buildRequest(body: any): REQUEST;

  protected abstract execute(request: REQUEST): Promise<CustomResponse<RESPONSE>>;

  async run(payload: string, context: Context): Promise<CustomResponse<RESPONSE>> {
    console.log("payload input --> ", payload);

    context.callbackWaitsForEmptyEventLoop = false;
    const request = this.buildRequest(payload);
    try {
      const response = await this.execute(request);
      return response;
    } catch (error) {
      console.log({ error: JSON.stringify(error) });

      return internalServerError({ error: "internal server error" }) as unknown as CustomResponse<RESPONSE>;
    }
  }
}
