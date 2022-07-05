import axios, { AxiosInstance } from "axios";

import { ResultMessage } from "./@types/telegram-types";

export class TelegramService {
  protected readonly api: AxiosInstance;

  private baseUrl?: string;

  private token?: string;

  private chatId?: string;

  constructor() {
    this.token = process.env.TELEGRAM_BOT_TOKEN;
    this.baseUrl = process.env.TELEGRAM_API_URL;
    this.chatId = process.env.TELEGRAM_CHAT_GROUP_ID;

    if (!this.token || !this.baseUrl || !this.chatId) {
      throw new Error("Credentials not configured");
    }

    this.api = axios.create({ baseURL: `${this.baseUrl}/${this.token}` });
  }

  public async sendNotification(text: string): Promise<ResultMessage> {
    const { data } = await this.api.get<ResultMessage>("/sendMessage", {
      data: {
        chat_id: this.chatId,
        text,
        parse_mode: "HTML"
      }
    });

    return data;
  }
}
