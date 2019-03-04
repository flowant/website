import { LocalDateTime, ZoneId } from 'js-joda';
import 'js-joda-timezone';
import { v1 } from 'uuid';

// deserialize class instances from json string.
export function reviver(key, value): any {
  switch (key) {
    case 'zoneId': {
      return ZoneId.of(value);
    }
    case 'created':
    case 'updated':
    case 'read': {
      return LocalDateTime.parse(value);
    }
    default: {
      return value;
    }
  }
}

export interface HasIdCid {
  idCid: IdCid;
}

export class IdCid {
  identity: string;
  containerId: string;

  constructor(identity: string, containerId: string) {
    this.identity = identity;
    this.containerId = containerId;
  }

  toPath(): string {
    return `/${this.identity}/${this.containerId}`;
  }

  static random(containerId?: string): IdCid {
    return new IdCid(v1(), containerId ? containerId : v1());
  }

}

export class Content {
  idCid: IdCid = IdCid.random();
  title: string = "Please type a title here.";
  extend: Extend = new Extend();
  fileRefs?: (FileRefs)[] = [];
  sentences: string = "Please type directions here.";;
  tags?: (string)[] | null; // Set type
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
  reviewerId: string;
  reviewerName: string;
  reputing: Reputation = new Reputation();
  comment: string = "Please type comments here.";
  reputation: Reputation = new Reputation();
  cruTime: CruTime = new CruTime();
}

export class Reply {
  idCid: IdCid;
  replierId: string;
  replierName: string;
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

export class User {
  identity: string;
  username: string;
  password: string;
  email: string;
  firstname: string;
  lastname: string;
  gender: string;
  birthdate: Birthdate;
  phone: Phone;
  address: Address;
  authorities?: (Authority)[] | null;
  followers?: (string)[] | null; //TODO Set type
  followings?: (string)[] | null; //TODO Set type
  interests?: (string)[] | null; //TODO Set type
  cruTime: CruTime;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
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

export class Authority {
  authority: string;
}

export class CruTime {
  zoneId: ZoneId = ZoneId.systemDefault();
  created: LocalDateTime = LocalDateTime.now();
  read: LocalDateTime = this.created;
  updated: LocalDateTime = this.created;
}
