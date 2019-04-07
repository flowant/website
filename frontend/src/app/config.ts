import { environment as env } from '../environments/environment';
import { Content, Review, Reply, User, Message, Relation, WebSite, Notification } from './_models';

export class Config {

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
  static signupUrl = Config.userUrl + env.path.signup;
  static authUrl = Config.gatewayUrl + env.path.auth;
  static webSiteUrl = Config.gatewayUrl + env.path.website;
  static imgServerUrl = Config.fileUrl;

  static gateway = env.gateway;
  static path = env.path;
  static auth = env.auth;
  static paging = env.paging;
  static webSite = env.webSite;

  static whitelistedDomains = [Config.gateway.domain + ':' + Config.gateway.port];
  static blacklistedRoutes = [Config.gateway.domain + ':' + Config.gateway.port + Config.path.gateway + Config.path.auth];

  static getUrl(type: any): string {
    switch(type) {
      case Content:{
        return Config.contentUrl;
      }
      case Review:{
        return Config.reviewUrl;
      }
      case Reply:{
        return Config.replyUrl;
      }
      case User:{
        return Config.userUrl;
      }
      case Message:{
        return Config.messageUrl;
      }
      case Notification:{
        return Config.notificationUrl;
      }
      case Relation:{
        return Config.relationUrl;
      }
      case WebSite:{
        return Config.webSiteUrl;
      }
      case "ContentRpt":{
        return Config.contentRptUrl;
      }
      case "ReviewRpt":{
        return Config.reviewRptUrl;
      }
      case "ReplyRpt":{
        return Config.replyRptUrl;
      }
    }
  }

  static toRptName(typeName: string): any {
    return typeName + 'Rpt';
  }

  static RECIPE = "recipe";
}