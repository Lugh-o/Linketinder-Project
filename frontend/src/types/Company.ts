import type { Address } from "./Address";
import { Person } from "./Person";

export class Company extends Person {
	idCompany: number;
	cnpj: string;
	name: string;

	constructor(
		idPerson: number,
		idCompany: number,
		email: string,
		passwd: string,
		description: string,
		address: Address,
		cnpj: string,
		name: string
	) {
		super(idPerson, email, passwd, description, address);
		this.idCompany = idCompany;
		this.cnpj = cnpj;
		this.name = name;
	}
}
