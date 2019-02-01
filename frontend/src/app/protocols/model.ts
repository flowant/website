export class Content {
    id: string;
    title: string;
    extend: Extend;
    fileRefs?: (FileRefsEntity)[] | null;
    sentences: string;
    tags?: (TagsEntity)[] | null;
    review: Review;
    cruTime: CruTime;
}
export class Extend {
    ingredients?: (string)[] | null;
    prepareSeconds: number;
    cookSeconds: number;
    servings: number;
    calory: number;
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
    reputation: Reputation;
    replies?: (RepliesEntity)[] | null;
}
export class Reputation {
    rating: number;
    liked: number;
    reported: number;
}
export class RepliesEntity {
    content: string;
    reputation: Reputation;
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
