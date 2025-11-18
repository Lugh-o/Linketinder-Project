import type { Address } from "./Address.ts";
import { Person } from "./Person.ts";

export class Candidate extends Person {
	idCandidate: number;
	firstName: string;
	lastName: string;
	cpf: string;
	birthday: string;
	graduation: string;
	competencies: string[];

	constructor(
		idCandidate: number,
		idPerson: number,
		email: string,
		passwd: string,
		description: string,
		address: Address,
		firstName: string,
		lastName: string,
		cpf: string,
		birthday: string,
		graduation: string,
		competencies: string[]
	) {
		super(idPerson, email, passwd, description, address);
		this.idCandidate = idCandidate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cpf = cpf;
		this.birthday = birthday;
		this.graduation = graduation;
		this.competencies = competencies;
	}
}
