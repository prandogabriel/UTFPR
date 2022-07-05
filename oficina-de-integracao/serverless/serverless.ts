import type { AWS, AwsLambdaEnvironment } from "@serverless/typescript";

import { changeDoorStatusTopic, photoTopic, serverlessResources, serverlessRoles } from "./configuration";

const service = "oficina-integracao";

const configuration: AWS = {
  service,
  variablesResolutionMode: "20210326",
  frameworkVersion: ">=1.83.3",
  functions: {
    "new-access-request": {
      handler: "src/handlers.newAccessRequest",
      timeout: 20,
      events: [{ http: { method: "POST", path: "new-access" } }]
    },
    "open-close-door": {
      handler: "src/handlers.openCloseDoor",
      events: [{ http: { method: "PATCH", path: "door" } }]
    },
    "notify-user-subscriber": {
      handler: "src/handlers.notifyUser",
      events: [
        {
          sns: {
            arn: `arn:aws:sns:\${aws:region}:\${aws:accountId}:${photoTopic}`
          }
        }
      ]
    },
    "notify-user-check-subscriber": {
      handler: "src/handlers.notifyUserCheckFaceAccessStatus",
      events: [
        {
          sns: {
            arn: `arn:aws:sns:\${aws:region}:\${aws:accountId}:${changeDoorStatusTopic}`
          }
        }
      ]
    },
    "check-face-access-subscriber": {
      handler: "src/handlers.checkFaceAccess",
      events: [
        {
          sns: {
            arn: `arn:aws:sns:\${aws:region}:\${aws:accountId}:${photoTopic}`
          }
        }
      ]
    }
  },
  useDotenv: true,
  provider: {
    name: "aws",
    runtime: "nodejs14.x",
    lambdaHashingVersion: "20201221",
    stage: "${self:custom.stage}",
    region: "us-east-1",
    versionFunctions: false,
    timeout: 10,
    memorySize: 512, // in mb
    // tracing: {
    //   apiGateway: true,
    //   lambda: true
    // },
    environment: {
      STAGE: "${opt:stage,'dev'}",
      AWS_ACCOUNT_REGION: "${aws:region}",
      TELEGRAM_BOT_TOKEN: "${env:TELEGRAM_BOT_TOKEN}",
      TELEGRAM_API_URL: "${env:TELEGRAM_API_URL}",
      TELEGRAM_CHAT_GROUP_ID: "${env:TELEGRAM_CHAT_GROUP_ID}",
      REKOGNITION_COLLECTION_ID: "${env:REKOGNITION_COLLECTION_ID}",
      AWS_IOT_ENDPOINT: "${env:AWS_IOT_ENDPOINT}",
      AWS_ACCOUNT_ID: "${aws:accountId}"
    } as AwsLambdaEnvironment,
    iamRoleStatements: serverlessRoles
  },
  custom: {
    stage: "${opt:stage,'dev'}",
    ssm: {}, // "${ssm:/aws/reference/secretsmanager/secret-name}",

    webpack: {
      webpackConfig: "./webpack.config.js",
      includeModules: true,
      packager: "yarn"
    }
  },
  resources: serverlessResources,
  package: {
    // individually: true,
    excludeDevDependencies: true
  },
  plugins: ["serverless-webpack", "serverless-offline"]
};

module.exports = configuration;
