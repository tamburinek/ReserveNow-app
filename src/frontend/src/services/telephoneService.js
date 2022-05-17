const generator = () => {
    let x = ((Math.floor(Math.random() * 10)) % 2)
    let tel = "";
    x === 2 ? tel = "602 " : x === 0 ? tel = "723 " : tel = "608 "
    for (let i = 0; i < 6; i++) {
        if (i % 3 === 0 && i !== 0) tel += " "
        tel += (String(((Math.floor(Math.random() * 10)) % 8) + 1))
    }
    return tel;
}

const TelephoneService = {
    generator
}

export default TelephoneService