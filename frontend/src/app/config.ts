import { environment as env } from '../environments/environment';

export enum Model {
  Content,
  Review,
  Reply
}

export class Config {
  static gatewayUrl = env.url.gateway;
  static contentUrl = env.url.gateway + env.path.content;
  static reviewUrl = env.url.gateway + env.path.review;
  static replyUrl = env.url.gateway + env.path.reply;
  static fileUrl = env.url.gateway + env.path.files;
  static fileDeletesUrl = Config.fileUrl + env.path.deletes;
  static popularPath = env.path.popular;

  static getUrl(model: Model): string {
    switch (model) {
      case Model.Content: {
        return Config.contentUrl;
      }
      case Model.Review: {
        return Config.reviewUrl;
      }
      case Model.Reply: {
        return Config.replyUrl;
      }
    }
  }
}