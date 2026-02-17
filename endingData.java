public class endingData {
    
    // บทสนทนาสำหรับ Akari Good Ending
    public static DialogueLine[] getAkariGoodEnding() {
        return new DialogueLine[] {
            new DialogueLine("อาคาริ", "ฮึก... ในที่สุดมันก็จบลงจริงๆ สักทีนะ นายรู้ไหม... ตอนที่ฉันอยู่กลางสนามแข่ง ฉันคิดแค่ว่าถ้าไม่มีนายตะโกนเชียร์อยู่ตรงนั้น ฉันคงจะก้าวขาไม่ออกไปแล้ว", "a"),
            new DialogueLine("พระเอก", "ผมบอกแล้วไงว่าผมจะรอรับเธอที่เส้นชัยเอง เห็นไหมว่าเธอทำได้!", "a"),
            new DialogueLine("อาคาริ", "ขอบคุณนะ... ขอบคุณที่เชื่อมั่นในตัวฉัน มากกว่าที่ฉันเชื่อมั่นในตัวเองซะอีก... จากนี้ไป ตำแหน่ง 'ถังออกซิเจน' และคนข้างๆ ของฉัน... ฉันขอจองให้นายคนเดียวตลอดไปเลยนะ!\"", "a"),
        };
    }

    public static DialogueLine[] getAkariBadEnding() {
        return new DialogueLine[] {
            new DialogueLine(null, null, null)
        };
    }

    // คุณสามารถเพิ่มเมธอด getAkariBadEnding() ไว้ที่นี่ได้เช่นกันครับ
}