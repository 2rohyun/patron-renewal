onmessage = function (data){

    // 메시지 받으면 스타트
    console.log('your message: '+data)
    console.log('my answer: YES')

    let start = new Date()
    console.log(start)
    const max_number = 100000
    let numbers = []
    for ( let i = 2 ; i <= max_number; i++) {
        numbers.push(i)
    }

    console.log('숫자 입력 완료')
    console.log(numbers)

    for ( let i = 2 ; i < max_number; i++) {
        let new_number = numbers.filter(e => e % i !== 0 || e === i);
        if( i % 100 === 0) {
            let percent = Math.round(i/max_number * 100 * 10) / 10 ;
            postMessage( percent + '%')
        }
        numbers = new_number
    }
    let end = new Date()

    console.log(end)
    let time = end - start

    console.log('success')
    console.log('걸린 시간(초): ' + Math.round(time/100)/10)
    postMessage(numbers)
}