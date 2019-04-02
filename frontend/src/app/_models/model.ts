import { LocalDateTime, ZoneId } from 'js-joda';
import 'js-joda-timezone';
import { v1 } from 'uuid';

// deserialize class instances from json string.
export function reviver(key, value): any {
  switch (key) {
    case 'idCid': {
      return IdCid.of(value['identity'], value['containerId']);
    }
    case 'cruTime': {
      return CruTime.assign(value);
    }
    case 'time': {
      return ZonedTime.assign(value);
    }
    case 'birthdate': {
      return Object.assign(new Birthdate(), value);
    }
    case 'phone': {
      return Object.assign(new Phone(), value);
    }
    case 'postalAddress': {
      return Object.assign(new PostalAddress(), value);
    }
    case 'reputing':
    case 'reputation': {
      return Object.assign(new Reputation(), value);
    }
    case 'authorities': {
      return new Set(value.map(e => Authority.of(e.authority)));
    }
    case 'fileRefs': {
      return value ? value.map(e =>Object.assign(new FileRefs(), e)) : [];
    }
    case 'tags':
    case 'followings':
    case 'followers':
    case 'subscribers':
    case 'likes':
    case 'interests': {
      return new Set(value);
    }
    case 'ingredients': {
      return value ? value : [];
    }
    default: {
      return value;
    }
  }
}

export function replacer(key, value): any {
  switch (key) {
    case 'authorities':
    case 'tags':
    case 'followings':
    case 'followers':
    case 'subscribers':
    case 'likes':
    case 'interests': {
      // from Set, "{ }" to Array, "[ ]"
      return Array.from(value.values());
    }
    default: {
      return value;
    }
  }
}

export interface HasIdCid {
  idCid: IdCid;
}

export interface IdToPath {
  toString(): string;
}

export class IdCid {
  identity: string;
  containerId: string;

  toString(): string {
    return `${this.identity}/${this.containerId}`;
  }

  static of(identity: string, containerId: string): IdCid {
    let idCid = new IdCid();
    idCid.identity = identity;
    idCid.containerId = containerId;
    return idCid;
  }

  static random(containerId?: string): IdCid {
    return IdCid.of(v1(), containerId ? containerId : v1());
  }

}

export class Content {
  idCid: IdCid;
  authorId: string;
  authorName: string;
  title: string;
  extend: Extend = new Extend();
  fileRefs: Array<FileRefs> = [];
  sentences: string;
  tags: Set<string> = new Set();
  reputation: Reputation = new Reputation();
  cruTime: CruTime = CruTime.now();

  static of(user: User, containerId: string): Content {
    let content = new Content();
    content.idCid = IdCid.random(containerId);
    content.authorId = user.identity;
    content.authorName = user.displayName;
    return content;
  }

  static random(): Content {
    let cid = v1();
    let user = User.random();
    let content = Content.of(user, cid);

    let randomStr = cid.substring(0, 8);
    content.title = randomStr;
    content.sentences = randomStr;
    content.tags.add('tag');
    return content;
  }
}

export class Extend {
  ingredients: Array<string> = [];
  prepareTime: string;
  cookTime: string;
  servings: number;
  calory: number;
  nutritionFacts: string;
}

export class FileRefs {
  identity: string;
  uri: string;
  contentType: string;
  filename: string;
  length: number;
  cruTime: CruTime;
}

export class Review {
  idCid: IdCid;
  authorId: string;
  authorName: string;
  reputing: Reputation = new Reputation();
  comment: string;
  reputation: Reputation = new Reputation();
  cruTime: CruTime = CruTime.now();

  static of(containerId:string, user: User, comment?:string): Review {
    let review = new Review();
    review.idCid = IdCid.random(containerId);
    review.authorId = user.identity;
    review.authorName = user.displayName;
    if (comment) {
      review.comment = comment;
    }
    return review;
  }

  static random(user: User): Review {
    let identity = v1();
    return Review.of(identity, user, 'comment' + identity);
  }

}

export class Reply {
  idCid: IdCid;
  authorId: string;
  authorName: string;
  comment: string = "Please type comments here.";
  reputation: Reputation = new Reputation();
  cruTime: CruTime = CruTime.now();
}

export class Reputation {
  idCid?: IdCid;
  viewed: number = 1;
  rated: number = 0;
  liked: number = 0;
  disliked: number = 0;
  reported: number = 0;
  reputed: number = 0;

  subtract(r: Reputation): Reputation {
    let diff = Object.assign(new Reputation(), this);
    diff.viewed -= r.viewed;
    diff.rated -= r.rated;
    diff.liked -= r.liked;
    diff.disliked -= r.disliked;
    diff.reported -= r.reported;
    diff.reputed -= r.reputed;
    return diff;
  }

  negative(): Reputation {
    let n = new Reputation();
    if(this.idCid) {
      n.idCid = this.idCid;
    }
    n.viewed = -this.viewed;
    n.rated = -this.rated;
    n.liked = -this.liked;
    n.disliked = -this.disliked;
    n.reported = -this.reported;
    n.reputed = -this.reputed;
    return n;
  }

  rate(rating: number): Reputation {
    this.rated = rating;
    this.reputed = 1;
    return this;
  }

  adjust(): Reputation {
    if (this.rated > 0) {
      this.reputed = 1;
    }
    return this;
  }

  avrRated(): string {
    return this.reputed ? (this.rated / this.reputed).toFixed(1) : '0';
  }

  select(key: string, increase: boolean): Reputation {
    //TODO assert this.hasOwnProperty('key')
    this.liked = 0;
    this.disliked = 0;
    this.reported = 0;
    this[key] = increase ? 1 : -1;
    return this;
  }

}

export class Message {
  idCid: IdCid;
  receiverName: string;
  authorId: string;
  authorName: string;
  sentences: string;
  markedAsRead: boolean = false;
  time: ZonedTime = ZonedTime.now();

  static of(receiverId: string, receiverName: string, senderId: string, senderName: string, sentences: string): Message {
    let msg: Message = new Message();
    msg.idCid = IdCid.random(receiverId);
    msg.receiverName = receiverName;
    msg.authorId = senderId;
    msg.authorName = senderName;
    msg.sentences = sentences;
    return msg;
  }

  static fromUser(receiver: User, sender: User, sentences: string): Message {
    return Message.of(receiver.identity, receiver.displayName, sender.identity, sender.displayName, sentences);
  }

}

export class Relation {

  static empty: Relation = Relation.of('empty', new Set(), new Set());

  identity: string;
  followings: Set<string> = new Set();
  followers: Set<string> = new Set();

  hasFollowee(userRefId: string): boolean {
    return this.followings.has(userRefId);
  }

  static of(identity:string , followings: Set<string>, followers: Set<string>): Relation {
    let relation = new Relation();
    relation.identity = identity;
    relation.followings = followings;
    relation.followers = followers;
    return relation;
  }

}

export class Notification {
  idCid: IdCid;
  authorName: string;
  category: Category;
  subscribers: Set<string> = new Set();
  referenceId: string;
  referenceCid: string;
  appendix: string;
  time: ZonedTime = ZonedTime.now();

  toString(): string {
    return Category.toString(this.category);
  }

  static of(authorId:string, authorName: string, category: Category, subscriberId?: string,
      referenceId?: string, referenceCid?: string, appendix?: string): Notification {

    let noti = new Notification();
    noti.idCid = IdCid.random(authorId);
    noti.authorName = authorName;
    noti.category = category;
    // In case of New Content, followers are added to subscribers in backend.
    if (subscriberId) {
      noti.subscribers.add(subscriberId);
    }
    if (referenceId) {
      noti.referenceId = referenceId;
    }
    if (referenceCid) {
      noti.referenceCid = referenceCid;
    }
    if (appendix) {
      noti.appendix = appendix;
    }
    return noti;
  }

  public static random(subscriberId: string): Notification {
    let randomId: string = v1();
    let randomStr = randomId.substring(0, 8);
    return Notification.of(randomId, randomStr, Category.Like, subscriberId);
  }

}

export namespace Category {
  export function toString(category: Category): string {
    switch(category) {
      case Category.NewContent:
        return "created a new content";
      case Category.NewReview:
        return "created a new review";
      case Category.NewReply:
        return "created a new reply";
      case Category.Like:
        return "likes";
      default:
        return "";
    }
  }
}

export enum Category {
  NewContent = "NC",  // new Content, notify to followers
  NewReview = "NRV", // new Review
  NewReply = "NRP", // new Reply
  Like = "L"   // like
}

export class WebSite {
  static empty: WebSite = new WebSite();

  identity: string;
  contentContainerIds: Map<string, string>;
  popularTagCounts?: null;
}

export class User {
  identity: string;
  username: string;
  password: string;
  email: string;
  firstname: string;
  lastname: string;
  displayName: string;
  gender: Gender = Gender.Undefined;
  birthdate: Birthdate;
  phone: Phone = new Phone();
  postalAddress: PostalAddress;
  authorities: Set<Authority> = new Set();
  likes: Set<string> = new Set();
  interests: Set<string> = new Set();
  fileRefs: Array<FileRefs> = [];
  cruTime: CruTime;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;

  isGuest(): boolean {
    return this.authorities.has(Authority.Guest) && this.authorities.size === 1;
  }

  isUser(): boolean {
    return this.authorities.has(Authority.User);
  }

  isAdmin(): boolean {
    return this.authorities.has(Authority.Admin);
  }

  isMe(identity: string): boolean {
    return this.identity === identity;
  }

  public static of(username: string, email: string, password: string): User {
    let user = new User();

    user.username = username;
    user.email = email;
    user.password = password;

    user.identity = v1();
    user.displayName = user.username;
    user.authorities = new Set().add(Authority.User);
    user.cruTime = CruTime.now();
    return user;
  }

  public static random(): User {
    let randomStr: string = v1();
    randomStr = randomStr.substring(0, 3);
    return User.of("User" + randomStr, randomStr + "@domain.com", randomStr);
  }

  public static guest(): User {
    let user = new User();
    user.identity = v1();
    user.displayName = 'Guest';
    user.authorities = new Set().add(Authority.Guest);
    user.cruTime = CruTime.now();
    return user;
  }

}

export enum Gender {
  Male = "M", // Male
  Female = "F", // Female
  Undefined = "U" // undefined
}

export class Birthdate {
  zoneId: string;
  localDate: string;
}

export class Phone {
  countryCode: number;
  nationalNumber: number;
}

export class PostalAddress {
  address: string;
  city: string;
  state: string;
  country: string;
  zipCode: string;
  detailCode?: null;
}

export enum Role {
  Admin = "A", // ADMIN
  User = "U", // USER
  Guest = "G" // GUEST
}

export class Authority {

  static Admin = Authority.from(Role.Admin);
  static User = Authority.from(Role.User);
  static Guest = Authority.from(Role.Guest);

  authority: Role;

  private static from(role: Role): Authority {
    let auth = new Authority();
    auth.authority = role;
    return auth;
  }

  static of(role: Role): Authority {
    switch (role) {
      case Role.Admin:
        return Authority.Admin;
      case Role.User:
        return Authority.User;
      default:
        return Authority.Guest;
    }
  }

}

export class CruTime {
  zoneId: ZoneId;
  created: LocalDateTime;
  read: LocalDateTime;
  updated: LocalDateTime;

  static now(): CruTime {
    let t = new CruTime();
    t.zoneId = ZoneId.systemDefault();
    t.created = LocalDateTime.now();
    t.read = LocalDateTime.now();
    t.updated = LocalDateTime.now();
    return t;
  }

  static assign(object: Object): CruTime {
    let t = new CruTime();
    t.zoneId = ZoneId.of(object['zoneId']);
    t.created = LocalDateTime.parse(object['created']);
    t.read = LocalDateTime.parse(object['read']);
    t.updated = LocalDateTime.parse(object['updated']);
    return t;
  }

}

export class ZonedTime {
  zoneId: ZoneId;
  created: LocalDateTime;

  static now(): ZonedTime {
    let t = new ZonedTime();
    t.zoneId = ZoneId.systemDefault();
    t.created = LocalDateTime.now();
    return t;
  }

  static assign(object: Object): ZonedTime {
    let t = new ZonedTime();
    t.zoneId = ZoneId.of(object['zoneId']);
    t.created = LocalDateTime.parse(object['created']);
    return t;
  }

}

export class RespWithLink<T> {
  response: T[];
  link: string;
  static parse = require('parse-link-header');

  getNextQueryParams(): string {
    if(!this.link) return null;
    var parsed = RespWithLink.parse(this.link);
    let index = parsed.next.url.indexOf("?");
    return parsed.next.url.substr(index);
  }

  static of<T>(response: T[], link: string): RespWithLink<T> {
    let respWithLink = new RespWithLink<T>();
    respWithLink.response = response;
    respWithLink.link = link;
    return respWithLink;
  }

}

export class Auth {
  access_token: string;
  token_type: string;
  refresh_token?: string;
  expires_in: number;
  scope: string;
  jti: string;
  username?: string;
}
