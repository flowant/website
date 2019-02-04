import * as uuid from 'uuid';

export class Content {
  id: string = uuid.v4();
  title: string = "Please type a title here.";
  extend: Extend = new Extend();
  fileRefs?: (FileRefsEntity)[] | null;
  sentences: string;
  tags?: (TagsEntity)[] | null;
  review: Review = new Review();
  cruTime: CruTime;
}
export class Extend {
  ingredients?: (string)[] | null  = ["Please type ingredients in separated lines.", "e.g., 6 ounces mozzarella cheese, shredded."];
  prepareTime: string = "10m30s";
  cookTime: string = "1h10m";
  servings: number = 1;
  calory: number = 0;
  nutritionFacts: string;
  prepareDuration: string;
  cookDuration: string;
}
export class FileRefsEntity {
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
  replies?: (RepliesEntity)[] | null;
}
export class Reputation {
  rating: number = 0;
  liked: number = 0;
  reported: number =0;
}
export class RepliesEntity {
  content: string = "Please type a title here.";
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
  zoneId: string;
  created: string;
  read: string;
  updated: string;
}
