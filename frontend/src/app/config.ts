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

  static gatewayUrl = env.url.gateway;
  static searchUrl = env.url.gateway + env.path.search;
  static contentUrl = env.url.gateway + env.path.content;
  static reviewUrl = env.url.gateway + env.path.review;
  static replyUrl = env.url.gateway + env.path.reply;
  static contentRptUrl = env.url.gateway + env.path.contentRpt;
  static reviewRptUrl = env.url.gateway + env.path.reviewRpt;
  static replyRptUrl = env.url.gateway + env.path.replyRpt;
  static fileUrl = env.url.gateway + env.path.files;
  static fileDeletesUrl = Config.fileUrl + env.path.deletes;
  static messageUrl = env.url.gateway + env.path.message;
  static notificationUrl = env.url.gateway + env.path.notification;
  static relationUrl = env.url.gateway + env.path.relation;
  static userUrl = env.url.gateway + env.path.user;
  static imgServerUrl = Config.fileUrl;

  static popularPath = env.path.popular;

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