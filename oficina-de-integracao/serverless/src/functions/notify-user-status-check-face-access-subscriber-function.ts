import { TelegramService } from "@services/telegram-service";

import { FunctionSubscriberAbstract } from "./abstracts/function-subscriber-abstract";
import { DoorStatusEnum } from "./enums/door-status-enum";

type Message = { status: DoorStatusEnum; name: string; confidence: string };

export class NotifyUserStatusCheckFaceAccessSubscriberFunction extends FunctionSubscriberAbstract<Message> {
  static instance: NotifyUserStatusCheckFaceAccessSubscriberFunction;

  protected async onErrorOnMessage(error: Error, message: Message): Promise<void> {
    console.log("Error on subscriber");
    console.log({ error, message });
  }

  protected buildRequest(request: string): Message {
    return JSON.parse(request);
  }

  protected async execute({ status, name, confidence }: Message): Promise<void> {
    const message =
      status === DoorStatusEnum.CLOSE
        ? "Tentativa não identificada, porta não aberta"
        : `Pessoa reconhecida ${name}, com confiança de ${parseInt(
            confidence,
            10
          )}%, porta aberta às ${new Date().toLocaleDateString()} ${new Date().toLocaleTimeString()}`;

    const telegramService = new TelegramService();

    console.log("Enviando mensagem para telegram", message);

    const resultMessage = await telegramService.sendNotification(message);

    console.log("Resultado envio mensage", JSON.stringify(resultMessage));
  }

  static getInstance() {
    if (!this.instance) {
      this.instance = new this();
    }

    return this.instance;
  }
}
