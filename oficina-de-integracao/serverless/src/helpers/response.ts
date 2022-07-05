import { CustomResponse } from "@functions/@types";

const OK = 200;

const CREATED = 201;

const NO_CONTENT = 204;

const BAD_REQUEST = 400;

const INTERNAL_SERVER_ERROR = 500;

const build = (httpCode: number, payload = {}): CustomResponse => {
  return {
    statusCode: httpCode,
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Credentials": true
    },
    body: JSON.stringify(payload)
  };
};

export const ok = (body?: any): CustomResponse => {
  return build(OK, body);
};

export const badRequest = (body?: any): CustomResponse => {
  return build(BAD_REQUEST, body);
};

export const created = (body?: any): CustomResponse => {
  return build(CREATED, body);
};

export const noContent = (): CustomResponse => {
  return build(NO_CONTENT);
};

export const internalServerError = (body?: any): CustomResponse => {
  return build(INTERNAL_SERVER_ERROR, body);
};

export const httpCode = (code: number, body?: any): CustomResponse => {
  return build(code, body);
};
