export abstract class Person {
	readonly id: number;
	name: string;
	email: string;
	state: string;
	cep: string;
	description: string;

	constructor(
		id: number,
		name: string,
		email: string,
		state: string,
		cep: string,
		description: string
	) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.state = state;
		this.cep = cep;
		this.description = description;
	}
}
