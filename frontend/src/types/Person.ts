import type { Address } from "./Address";

export abstract class Person {
	idPerson: number;
	email: string;
	passwd: string;
	description: string;
	address: Address;

	constructor(
		idPerson: number,
		email: string,
		passwd: string,
		description: string,
		address: Address
	) {
		this.idPerson = idPerson;
		this.email = email;
		this.passwd = passwd;
		this.description = description;
		this.address = address;
	}
}
