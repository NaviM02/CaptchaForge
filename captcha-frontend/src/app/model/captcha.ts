export class Captcha {
    id!: string;
    name!: string;
    timesUsed!: number;
    successes!: number;
    fails!: number;
    lastUse!: Date;
    html!: string;

    
    constructor(id:string, name:string, timesUsed:number, susccesses:number, fails:number, lastUse:string, html:string){
        this.id = id;
        this.name = name;
        this.timesUsed = timesUsed;
        this.successes = susccesses;
        this.fails = fails;
        if(lastUse === 'null') this.lastUse;

        else this.lastUse = this.stringToDate(lastUse);
        this.html = html;
    }
    dbString(): string{
        return `{
            "ID": "${this.id}",
            "NOMBRE": "${this.name}",
            "USADO": ${this.timesUsed},
            "ACIERTOS": ${this.successes},
            "FALLOS": ${this.fails},
            "ULTIMO_USO": "${this.dateToString(this.lastUse)}",
            "HTML": "${this.html}"
          }`;
    }
    dateToString(date: Date){
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
    private stringToDate(dateString: string): Date {
        const [year, month, day] = dateString.split('-').map(Number);
        return new Date(year, month - 1, day);
    }
}
