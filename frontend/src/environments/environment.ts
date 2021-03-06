// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,

  webSite: {
    identity: 'f1b8dba2-44a4-11e9-944f-99e89c6a8c79'
  },
  gateway: {
    scheme: 'https',
    domain: 'gateway.flowant.org',
    port: 8443
  },
  path: {
    gateway: '/api',
    search: '/search',
    content: '/content',
    review: '/review',
    reply: '/reply',
    contentRpt: '/content_rpt',
    reviewRpt: '/review_rpt',
    replyRpt: '/reply_rpt',
    files: '/files',
    deletes: '/deletes',
    popular: '/popular',
    website: '/website',
    message: '/message',
    notification: '/notification',
    relation: '/relation',
    follow: '/follow',
    unfollow: '/unfollow',
    user: '/user',
    signup: '/signup',
    auth: '/oauth/token'
  },
  auth: {
    clientId: 'client',
    clientPass: 'client'
  },
  paging: {
    defaultPage: '0',
    defaultSize: '12',
    previewSize: '5'
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
