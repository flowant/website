import { environment as env } from '../environments/environment';

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
  static imgServerUrl = Config.fileUrl;

  static gateway = env.gateway;
  static path = env.path;
  static auth = env.auth;
  static paging = env.paging;

  static whitelistedDomains = [Config.gateway.domain + ':' + Config.gateway.port];
  static blacklistedRoutes = [Config.gateway.domain + ':' + Config.gateway.port + Config.path.gateway + Config.path.auth];

  // key: type name, value: url
  static urlMap: Map<string, string> = new Map([
    ['Content', Config.contentUrl],
    ['Review', Config.reviewUrl],
    ['Reply', Config.replyUrl],
    ['ContentRpt', Config.contentRptUrl],
    ['ReviewRpt', Config.reviewRptUrl],
    ['ReplyRpt', Config.replyRptUrl],
    ['User', Config.userUrl],
    ['Message', Config.messageUrl],
    ['Notification', Config.notificationUrl],
    ['Relation', Config.relationUrl]
  ]);

  static getUrl(typeName: string): string {
    return this.urlMap.get(typeName);
  }

  static toRptName(typeName: string): any {
    return typeName + 'Rpt';
  }

}