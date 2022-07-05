export const serverlessRoles = [
  {
    Effect: "Allow",
    Action: ["ssm:GetParameter"],
    Resource: "*"
  },
  {
    Effect: "Allow",
    Action: ["secretsmanager:GetSecretValue"],
    Resource: "*"
  },
  {
    Effect: "Allow",
    Action: ["rekognition:*"],
    Resource: "*"
  },
  {
    Effect: "Allow",
    Action: ["s3:GetObject", "s3:ListBucket", "s3:PutObject"],
    Resource: "*"
  },
  {
    Effect: "Allow",
    Action: ["iot:*"],
    Resource: "*"
  },
  {
    Effect: "Allow",
    Action: ["sns:Publish"],
    Resource: "*"
  },
  {
    Effect: "Allow",
    Action: [
      "dynamodb:Query",
      "dynamodb:Scan",
      "dynamodb:GetItem",
      "dynamodb:PutItem",
      "dynamodb:UpdateItem",
      "dynamodb:DeleteItem",
      "dynamodb:BatchWriteItem"
    ],
    Resource: "*"
  }
];
