/* eslint-disable camelcase */
export interface ResultMessage {
  ok: boolean;
  result: Result;
}

export interface Result {
  message_id: number;
  from: From;
  chat: Chat;
  date: number;
  text: string;
}

export interface From {
  id: number;
  is_bot: boolean;
  first_name: string;
  username: string;
}

export interface Chat {
  id: number;
  title: string;
  type: string;
  all_members_are_administrators: boolean;
}
