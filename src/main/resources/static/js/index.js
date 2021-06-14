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
function findPrime1(){
    const max_number = 100000
    let numbers = []
    for ( let i = 2 ; i <= max_number; i++) {
        numbers.push(i)
    }
    console.log('숫자 입력 완료')
    console.log(numbers)
    for ( let i = 2 ; i < max_number; i++) {
        let new_number = numbers.filter(e => e % i !== 0 || e === i);
        if( i % 1000 === 0) {
            document.getElementById('percent_1').innerText = i/max_number + '%'
        }
        numbers = new_number
    }
    console.log(numbers)
}

// function findPrime2(){
//     const max_number = 100000
//     let numbers = []
//     for ( let i = 2 ; i <= max_number; i++) {
//         numbers.push(i)
//     }
//     console.log('숫자 입력 완료')
//     console.log(numbers)
//     for ( let i = 2 ; i < max_number; i++) {
//         let new_number = numbers.filter(e => e % i !== 0 || e === i);
//         if( i % 1000 === 0) {
//             document.getElementById('percent_2').innerText = i/max_number + '%'
//         }
//         numbers = new_number
//     }
//     console.log(numbers)
// }

//web worker 테스트
function workerTest() {
    const worker = new Worker("js/worker.js");
    worker.postMessage('go')
    worker.onmessage = function (data){
        if( data['data'].length < 100) {
            document.getElementById('percent_2').innerText = data['data']
        } else {
            document.getElementById('percent_2').innerText = '완료'
            alert(data['data'])
        }
    }
}











