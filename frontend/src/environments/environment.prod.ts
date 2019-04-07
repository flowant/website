export const environment = {
  production: true,

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
