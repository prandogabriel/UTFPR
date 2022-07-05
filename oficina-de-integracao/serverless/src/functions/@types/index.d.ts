export interface CustomResponse<T = string> {
  statusCode: number;
  headers: {
    "Content-Type": string;
    "Access-Control-Allow-Origin": string;
    "Access-Control-Allow-Credentials": boolean;
  };
  body: T;
}

export interface HttpRequestWrapper<T> {
  body: T;
}
