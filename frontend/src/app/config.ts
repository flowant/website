import { environment as env } from '../environments/environment';

export enum Model {
  Content = "Content",
  Review = "Review",
  Reply = "Reply",
  ContentRpt = "ContentRpt",
  ReviewRpt = "ReviewRpt",
  ReplyRpt = "ReplyRpt",
  User = "User",
  Message = "Message",
  Notification = "Notification",
  Relation = "Relation"
}

export class Config {

  static AUTHENTICATION: string = 'authentication';

  static gatewayUrl = env.gateway.scheme + '://' + env.gateway.domain + ':' + env.gateway.port + env.path.gateway;
  static searchUrl = Config.gatewayUrl + env.path.search;
  static contentUrl = Config.gatewayUrl + env.path.content;
  static reviewUrl = Config.gatewayUrl + env.path.review;
  static replyUrl = Config.gatewayUrl + env.path.reply;
  static contentRptUrl = Config.gatewayUrl + env.path.contentRpt;
  static reviewRptUrl = Config.gatewayUrl + env.path.reviewRpt;
  static replyRptUrl = Config.gatewayUrl + env.path.replyRpt;
  static fileUrl = Config.gatewayUrl + env.path.files;
  static fileDeletesUrl = Config.fileUrl + env.path.deletes;
  static messageUrl = Config.gatewayUrl + env.path.message;
  static notificationUrl = Config.gatewayUrl + env.path.notification;
  static relationUrl = Config.gatewayUrl + env.path.relation;
  static userUrl = Config.gatewayUrl + env.path.user;
  static authUrl = Config.gatewayUrl + env.path.auth;
  static imgServerUrl = Config.fileUrl;

  static gateway = env.gateway;
  static path = env.path;
  static auth = env.auth;

  static defaultPage = "0";
  static defaultSize = "12";
  static previewSize = "5";

  static urlMap: Map<Model, string> = new Map([
    [Model.Content, Config.contentUrl],
    [Model.Review, Config.reviewUrl],
    [Model.Reply, Config.replyUrl],
    [Model.ContentRpt, Config.contentRptUrl],
    [Model.ReviewRpt, Config.reviewRptUrl],
    [Model.ReplyRpt, Config.replyRptUrl],
    [Model.User, Config.userUrl],
    [Model.Message, Config.messageUrl],
    [Model.Notification, Config.notificationUrl],
    [Model.Relation, Config.relationUrl],
  ]);

  static getUrl(model: Model): string {
    return this.urlMap.get(model);
  }

  static toRptModel(model: Model): Model {
    switch (model) {
      case Model.Content:
        return Model.ContentRpt;
      case Model.Reply:
        return Model.ReplyRpt;
      case Model.Review:
        return Model.ReviewRpt;
      default:
      //TODO error handle
    }
  }

}