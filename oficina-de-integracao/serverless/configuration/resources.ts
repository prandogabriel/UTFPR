import { facesBucket, facesTable, photoTopic } from "./variables";

export const serverlessResources = {
  Resources: {
    // Dynamo resources
    FacesTable: {
      Type: "AWS::DynamoDB::Table",
      DeletionPolicy: "Retain",
      Properties: {
        TableName: facesTable,
        // BillingMode: "PAY_PER_REQUEST", sob demanda não entra no plano free
        ProvisionedThroughput: {
          ReadCapacityUnits: 2,
          WriteCapacityUnits: 2
        },
        AttributeDefinitions: [
          {
            AttributeName: "id",
            AttributeType: "S"
          }
        ],
        KeySchema: [
          {
            AttributeName: "id",
            KeyType: "HASH"
          }
        ]
      }
    },

    // SNS resources
    PhotoTopic: {
      Type: "AWS::SNS::Topic",
      Properties: {
        DisplayName: "Receberá todas as fotos tiradas no ESP",
        TopicName: photoTopic
      }
    },
    ChangeDoorStatusTopic: {
      Type: "AWS::SNS::Topic",
      Properties: {
        DisplayName: "Receberá ordens de alteração de status da porta para aberto/fechado",
        TopicName: "CHANGE_DOOR_STATUS_TOPIC"
      }
    },

    // S3 resources
    FacesBucket: {
      Type: "AWS::S3::Bucket",
      Properties: {
        BucketName: facesBucket,
        AccessControl: "PublicRead"
      }
    },

    EspThing: {
      Type: "AWS::IoT::Thing",
      Properties: {
        AttributePayload: {
          Attributes: {
            SensorType: "soil"
          }
        }
      }
    },
    EspThingPolicy: {
      Type: "AWS::IoT::Policy",
      Properties: {
        PolicyDocument: {
          Version: "2012-10-17",
          Statement: [
            {
              Effect: "Allow",
              Action: ["iot:Connect", "iot:Publish"],
              Resource: ["*"]
            }
          ]
        }
      }
    },
    IoTRole: {
      Type: "AWS::IAM::Role",
      Properties: {
        AssumeRolePolicyDocument: {
          Version: "2012-10-17",
          Statement: [
            {
              Effect: "Allow",
              Principal: {
                Service: ["iot.amazonaws.com"]
              },
              Action: ["sts:AssumeRole"]
            }
          ]
        }
      }
    }
  },
  Outputs: {}
};
