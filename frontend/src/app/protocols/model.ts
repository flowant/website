import * as uuid from 'uuid';
import { LocalDateTime, ZoneId } from 'js-joda';
import 'js-joda-timezone';

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

export class Content {
  id: string = uuid.v4();
  title: string = "Please type a title here.";
  extend: Extend = new Extend();
  fileRefs?: (FileRefs)[] = [];
  sentences: string = "Please type directions here.";;
  tags?: (TagsEntity)[] | null;
  review: Review = new Review();
  cruTime: CruTime = new CruTime();
}

export class Extend {
  ingredients?: (string)[] | null = ["Please type ingredients in separated lines.", "e.g., 6 ounces mozzarella cheese, shredded."];
  prepareTime: string = "10m30s";
  cookTime: string = "1h10m";
  servings: number = 0;
  calory: number = 0;
  nutritionFacts: string;
  prepareDuration: string;
  cookDuration: string;
}

export class FileRefs {
  id: string;
  uri: string;
  contentType: string;
  filename: string;
  length: number;
  cruTime: CruTime;
}

export class TagsEntity {
  name: string;
}

export class Review {
  reputation: Reputation = new Reputation();
  replies?: (Reply)[] | null;
}

export class Reputation {
  rating: number = 0;
  liked: number = 0;
  reported: number = 0;
}

export class Reply {
  replierId: string;
  replierName: string;
  content: string = "Please type messages here.";
  reputation: Reputation = new Reputation();
  cruTime: CruTime;
}

export class User {
  id: string;
  username: string;
  password: string;
  email: string;
  firstname: string;
  lastname: string;
  gender: string;
  birthdate: Birthdate;
  phone: Phone;
  address: Address;
  authorities?: (AuthoritiesEntity)[] | null;
  followers?: (string)[] | null;
  followings?: (string)[] | null;
  interests?: (InterestsEntity)[] | null;
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

export class AuthoritiesEntity {
  authority: string;
}

export class InterestsEntity {
  name: string;
}

export class CruTime {
  zoneId: ZoneId = ZoneId.systemDefault();
  created: LocalDateTime = LocalDateTime.now();
  read: LocalDateTime = this.created;
  updated: LocalDateTime = this.created;
}
