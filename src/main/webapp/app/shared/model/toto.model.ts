export interface IToto {
  id?: string;
  name?: string;
}

export class Toto implements IToto {
  constructor(public id?: string, public name?: string) {}
}
