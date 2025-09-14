import type { Competency } from "./Competency.ts";
import { Person } from "./Person.ts";

export class Candidate extends Person {
	cpf: string;
	age: number;
	graduation: string;
	competencies: Competency[];

	constructor(
		id: number,
		name: string,
		email: string,
		state: string,
		cep: string,
		description: string,
		cpf: string,
		age: number,
		graduation: string,
		competencies: Competency[]
	) {
		super(id, name, email, state, cep, description);
		this.cpf = cpf;
		this.age = age;
		this.graduation = graduation;
		this.competencies = competencies;
	}
}
