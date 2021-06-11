// TODO th태그는 문서 로딩이 끝난 뒤에는 불가
let i = 0
const button = document.getElementById('button')
button.onclick = function (){
    i++
    let fragment = "test" + i;
    let test = document.createElement('div')
    test.setAttribute('th:replace',fragment)
    test.innerText = "append success, but not change"
    let k = document.getElementById('fragment')
    button.setAttribute('disabled','disabled')
    k.append(test)

    let year
    $.getJSON('https://bafybeicxlzmya2fn6kntihv6zazpzxjo7dghxpuhn2tkk3nopjikfc2xoi.ipfs.infura-ipfs.io/',function (result){
        year = result['attributes']['Year']
        console.log('정답'+ year)
    })
    if("2019" === year){
        console.log('success')
    } else {
        console.log('fail')
    }
    console.log('end')
}


//

setTimeout(function findPrime(){

    let new_button = document.createElement("button")
    new_button.setAttribute("id","find_prime")
    new_button.innerText = "찾아 보자"
    document.getElementById('primeTest').append(new_button)

    //
    new_button.onclick = function () {
        const max_number = parseInt(prompt())
        let numbers = []

        for ( let i = 2 ; i <= max_number; i++) {
            numbers.push(i)
        }

        console.log('숫자 입력 완료')
        console.log(numbers)

        for ( let i = 2 ; i < 4; i++) {
            let new_number = numbers.filter(e => e % i !== 0 && e !== i);
            numbers = new_number
            console.log(new_number)
        }

    }


},1000)













