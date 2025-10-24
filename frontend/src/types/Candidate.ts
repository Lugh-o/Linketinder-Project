import type { Competency } from "./Competency.ts";
import { Person } from "./Person.ts";

export class Candidate extends Person {
	cpf: string;
	age: number;
	graduation: string;
	competencies: Competency[];

	constructor(
		name: string,
		email: string,
		state: string,
		cep: string,
		description: string,
		cpf: string,
		age: number,
		graduation: string,
		competencies: Competency[],
		id?: number
	) {
		super(id ?? 0, name, email, state, cep, description);
		this.cpf = cpf;
		this.age = age;
		this.graduation = graduation;
		this.competencies = competencies;
	}
}
