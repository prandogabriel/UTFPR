import { Handler } from "aws-lambda/handler";

import { CheckFaceAccessSubscriberFunction } from "@functions/check-face-access-subscriber-function";
import { NewAccessRequestFunction } from "@functions/new-access-request-function";
import { NotifyUserStatusCheckFaceAccessSubscriberFunction } from "@functions/notify-user-status-check-face-access-subscriber-function";
import { NotifyUserSubscriberFunction } from "@functions/notify-user-subscriber-function";
import { OpenCloseDoorFunction } from "@functions/open-close-door-function";

export const newAccessRequest: Handler = (event, context) => NewAccessRequestFunction.getInstance().run(event, context);
export const openCloseDoor: Handler = (event, context) => OpenCloseDoorFunction.getInstance().run(event, context);
export const notifyUser: Handler = (event, context) => NotifyUserSubscriberFunction.getInstance().run(event, context);
export const notifyUserCheckFaceAccessStatus: Handler = (event, context) =>
  NotifyUserStatusCheckFaceAccessSubscriberFunction.getInstance().run(event, context);
export const checkFaceAccess: Handler = (event, context) =>
  CheckFaceAccessSubscriberFunction.getInstance().run(event, context);
