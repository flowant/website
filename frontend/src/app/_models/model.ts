import { LocalDateTime, ZoneId } from 'js-joda';
import 'js-joda-timezone';
import { v1 } from 'uuid';

// deserialize class instances from json string.
export function reviver(key, value): any {
  switch (key) {
    case 'idCid': {
      return new IdCid(value['identity'], value['containerId']);
    }
    case 'zoneId': {
      return ZoneId.of(value);
    }
    case 'created':
    case 'updated':
    case 'read': {
      return LocalDateTime.parse(value);
    }
    case 'followings':
    case 'followers':
    case 'likes':
    case 'interests':
      return new Set(value);
    case 'authorities':
      return value.map(e => Authority.of(e.authority));
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

  constructor(identity: string, containerId: string) {
    this.identity = identity;
    this.containerId = containerId;
  }

  toString(): string {
    return `${this.identity}/${this.containerId}`;
  }

  static random(containerId?: string): IdCid {
    return new IdCid(v1(), containerId ? containerId : v1());
  }

}

export class Content {
  idCid: IdCid = IdCid.random();
  authorId: string;
  authorName: string;
  title: string = "Please type a title here.";
  extend: Extend = new Extend();
  fileRefs?: (FileRefs)[] = [];
  sentences: string = "Please type directions here.";;
  tags?: (string)[] | null;
  reputation: Reputation = new Reputation();
  cruTime: CruTime = new CruTime();
}

export class Extend {
  ingredients?: (string)[] | null = ["Please type ingredients in separated lines.", "e.g., 6 ounces mozzarella cheese, shredded."];
  prepareTime: string = "10m30s";
  cookTime: string = "1h10m";
  servings: number = 0;
  calory: number = 0;
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
  cruTime: CruTime = new CruTime();
}

export class Reply {
  idCid: IdCid;
  authorId: string;
  authorName: string;
  comment: string = "Please type comments here.";
  reputation: Reputation = new Reputation();
  cruTime: CruTime = new CruTime();
}

export class Reputation {
  idCid?: IdCid;
  viewed: number = 0;
  rated: number = 0;
  liked: number = 0;
  disliked: number = 0;
  reported: number = 0;
  reputed: number = 0;

  subtract(r: Reputation): Reputation {
    let diff = Object.assign({}, this);
    diff.viewed -= r.viewed;
    diff.rated -= r.rated;
    diff.liked -= r.liked;
    diff.disliked -= r.disliked;
    diff.reported -= r.reported;
    diff.reputed -= r.reputed;
    return diff;
  }

  select(key: string): Reputation {
    //TODO assert this.hasOwnProperty('key')
    this.liked = 0;
    this.disliked = 0;
    this.reported = 0;
    this[key] = 1;
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
  time: ZonedTime = new ZonedTime();

  static of(receiverId: string, receiverName: string, senderId: string, senderName: string, sentences: string): Message {
    let msg: Message = new Message();
    msg.idCid = IdCid.random(receiverId);
    msg.receiverName = receiverName;
    msg.authorId = senderId;
    msg.authorName = senderName;
    msg.sentences = sentences;
    return msg;
  }

}

export class Relation {

  static empty: Relation = new Relation();

  identity: string;
  followings: Set<string> = new Set();
  followers: Set<string> = new Set();
}

export class Notification {
  idCid: IdCid;
  authorName: string;
  category: Category;
  subscribers?: (string)[] | null;
  referenceId: string;
  referenceCid: string;
  appendix: string;
  time: ZonedTime;
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
  address: Address;
  authorities: (Authority)[] = new Array();
  likes?: Set<string> | null;
  interests?: Set<string> | null;
  fileRefs?: (FileRefs)[] = [];
  cruTime: CruTime;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;

  static isGuest(user: User): boolean {
    return user.authorities.includes(Authority.Guest) && user.authorities.length === 1;
  }

  static isUser(user: User): boolean {
    return user.authorities.includes(Authority.User);
  }

  static isAdmin(user: User): boolean {
    return user.authorities.includes(Authority.Admin);
  }

  public static of(username: string, email: string, password: string): User {
    let user = new User();

    user.username = username;
    user.email = email;
    user.password = password;

    user.identity = v1();
    user.displayName = user.username;
    user.authorities.push(Authority.User);
    user.cruTime = new CruTime();
    return user;
  }

  public static guest(): User {
    let user = new User();
    user.identity = v1();
    user.displayName = 'Guest';
    user.authorities.push(Authority.Guest);
    user.cruTime = new CruTime();
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

export class Address {
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
  zoneId: ZoneId = ZoneId.systemDefault();
  created: LocalDateTime = LocalDateTime.now();
  read: LocalDateTime = this.created;
  updated: LocalDateTime = this.created;
}

export class ZonedTime {
  zoneId: ZoneId = ZoneId.systemDefault();
  created: LocalDateTime = LocalDateTime.now();
}

export class RespWithLink<T> {
  response: T[];
  link: string;
  static parse = require('parse-link-header');

  static of<T>(response: T[], link: string): RespWithLink<T> {
    let respWithLink = new RespWithLink<T>();
    respWithLink.response = response;
    respWithLink.link = link;
    return respWithLink;
  }

  getNextQueryParams(): string {
    if(!this.link) return null;
    var parsed = RespWithLink.parse(this.link);
    let index = parsed.next.url.indexOf("?");
    return parsed.next.url.substr(index);
  }
}

export interface Auth {
  access_token: string;
  token_type: string;
  refresh_token?: string;
  expires_in: number;
  scope: string;
  jti: string;
  username?: string;
}
