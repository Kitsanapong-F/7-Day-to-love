public class storyDataReina {
    
    public static String  getRaynaDayBackground(int day) {
        switch (day) {
            case 1: return  getRaynaDay1Backgroud();
            case 2: return  getRaynaDay2Backgroud();
            case 3: return  getRaynaDay3Backgroud();
            case 4: return  getRaynaDay4Backgroud();
            case 5: return  getRaynaDay5Backgroud();
            case 6: return  getRaynaDay6Backgroud();
            default: return null;
        }
    }

    public static DialogueLine[] getRaynaDayStory(int day) {
        switch (day) {
            case 1: return getRaynaDay1story();
            case 2: return  getRaynaDay2story();
            case 3: return  getRaynaDay3story();
            case 4: return  getRaynaDay4story();
            case 5: return  getRaynaDay5story();
            case 6: return  getRaynaDay6story();
            default: return null;
        }
    }

    public static String[]  getRaynaDayChoice(int day) {
        switch (day) {
            case 1: return  getRaynaDay1Choice();
            case 2: return  getRaynaDay2Choice();
            case 3: return  getRaynaDay3Choice();
            case 4: return  getRaynaDay4Choice();
            case 5: return  getRaynaDay5Choice();
            case 6: return  getRaynaDay6Choice();
            default: return new String[]{"...", "..."};
        }
    }

    public static DialogueLine[]  getRaynaDayResponseA(int day) {
        switch (day) {
            case 1: return  getRaynaDay1ResponseA();
            case 2: return  getRaynaDay2ResponseA();
            case 3: return  getRaynaDay3ResponseA();
            case 4: return  getRaynaDay4ResponseA();
            case 5: return  getRaynaDay5ResponseA();
            case 6: return  getRaynaDay6ResponseA();
            default: return null;
        }
    }

    public static DialogueLine[]  getRaynaDayResponseB(int day) {
        switch (day) {
            case 1: return  getRaynaDay1ResponseB();
            case 2: return  getRaynaDay2ResponseB();
            case 3: return  getRaynaDay3ResponseB();
            case 4: return  getRaynaDay4ResponseB();
            case 5: return  getRaynaDay5ResponseB();
            case 6: return  getRaynaDay6ResponseB();
            default: return null;
        }
    }



    public static String getRaynaDay1Backgroud(){
        return "image\\Naohiro.jpg";
    };
    public static DialogueLine[] getRaynaDay1story(){
        return new DialogueLine[] {
            new DialogueLine("เรย์น่า", "\"นายอีกแล้วเหรอ? ฉันจำได้ว่าสั่งห้ามคนนอกเข้าห้องสภาในช่วงเตรียมงานเทศกาลไม่ใช่เหรอ? หรือว่าภาษาญี่ปุ่นของฉันมันเข้าใจยากเกินไปสำหรับนายกันคะ?\"", "image\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
            new DialogueLine("พระเอก", "\"เปล่าครับ ผมแค่เห็นไฟห้องสภายังเปิดอยู่ ทั้งที่คนอื่นกลับกันหมดแล้ว... เลยเอาชาร้อนมาให้น่ะครับ\"", "image\\0aaa6b48-e691-4426-9321-dd7c1d29cc97.png"),
            new DialogueLine("เรย์น่า", "\"ชาร้อนเหรอ? ...นี่นายคิดจะติดสินบนประธานนักเรียนด้วยเครื่องดื่มราคาไม่กี่เยนเหรอคะ? หึ... แต่เอาเถอะ วางไว้ตรงนั้นแหละ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    }

    public static String[] getRaynaDay1Choice(){
        return new String[] {"ถ้าประธานยังไม่กลับ ผมขอนั่งช่วยงานตรงมุมนั้นเงียบๆ ได้ไหมครับ?", "ห้องนี้เงียบดีนะ ผมขอมานอนพักสักงีบได้ไหม?"};
    };

    public static DialogueLine[] getRaynaDay1ResponseA(){
        return new DialogueLine[] {
            new DialogueLine("เรย์นะ", "\"(ชะงักไปครู่หนึ่ง แววตาที่แข็งกร้าวอ่อนลงเล็กน้อย)\"นั่งช่วยงานเหรอ? ...ถ้าอยากช่วยจริงๆ ก็ช่วยจัดหมวดหมู่เอกสารกองนั้นทีสิ แต่อย่าทำเสียงดังรบกวนสมาธิฉันล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
            new DialogueLine("พระเอก", "\"รับทราบครับท่านประธาน! ผมจะเงียบเหมือนเป็นอากาศเลยล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
            new DialogueLine("เรย์นะ", "\"หึ... ปากดีจังนะ ขอบใจนะที่อยู่เป็นเพื่อน... ความจริงนั่งทำงานคนเดียวมันก็... นิดหน่อยน่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
        };
    };

    public static DialogueLine[] getRaynaDay1ResponseB(){
        return new DialogueLine[] {
            new DialogueLine("เรย์น่า", "\"(วางปากกากระแทกโต๊ะ)\"ที่นี่คือห้องสภานักเรียน ไม่ใช่ห้องนั่งเล่น! ถ้าจะนอนก็ไปที่ห้องสมุดนู่น! ฉันยุ่งมากจนไม่มีเวลามาล้อเล่นกับนายหรอกนะ!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
            new DialogueLine("พระเอก", "\"เอาน่า ผมแค่เห็นประธานเครียดเกินไปเลยอยากให้พักบ้าง\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
            new DialogueLine("เรย์นะ", "\"การพักของฉันไม่ใช่การมาเห็นนายอู้งานตรงหน้าค่ะ! ออกไปเดี๋ยวนี้เลยนะ!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    };
//==============================================================================================================================================================================
    public static String getRaynaDay2Backgroud(){
        return "image\\_convenience_store_2.jpg";
    };

    public static DialogueLine[] getRaynaDay2story(){
        return new DialogueLine[] {
            new DialogueLine("เรย์นะ", "\"เฮ้อ... ในที่สุดก็ตรวจเสร็จสักที ขอบใจนะที่อยู่ช่วยจนป่านนี้ ทั้งที่นายไม่ต้องทำก็ได้\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
            new DialogueLine("พระเอก", "\"ผมบอกแล้วไงว่าอยากช่วย ไม่อยากให้ประธานแบกทุกอย่างไว้คนเดียว\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
            new DialogueLine("เรย์นะ", "\"ทำไมต้องใจดีขนาดนั้นคะ? ทั้งที่ฉันเข้มงวดกับนายมาตลอด... นายไม่รำคาญบ้างเหรอ?\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
        };
    }

     public static String[] getRaynaDay2Choice(){
        return new String[] {"เพราะผมรู้ว่าภายใต้ความเข้มงวดนั้น ประธานคือคนที่ห่วงใยคนอื่นที่สุดไงครับ", "ก็รำคาญแหละครับ แต่ผมชอบเอาชนะคนดุน่ะ"};
    };

    public static DialogueLine[] getRaynaDay2ResponseA(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"(หน้าแดงวูบหนึ่งและหลบสายตา) \"พะ-พูดอะไรออกมาน่ะคะ... ใครห่วงใยใครกัน! ฉันก็แค่ทำตามหน้าที่... แต่ก็นะ... ขอบคุณที่มองเห็นมันนะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"\"หน้าแดงอยู่นะครับประธาน ยอมรับความจริงเถอะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("เรย์นะ", "\"เงียบไปเลยนะ! รีบกลับบ้านได้แล้ว พรุ่งนี้ห้ามสายล่ะ!\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
    };

     public static DialogueLine[] getRaynaDay2ResponseB(){
        return new DialogueLine[] {
            new DialogueLine("เรย์นะ", "\"(สีหน้ากลับมาเย็นชา) \"เอาชนะงั้นเหรอ? ...นายมองว่าความรู้สึกของฉันเป็นแค่เกมเหรอคะ? น่าผิดหวังจริงๆ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
            new DialogueLine("พระเอก", "\"ไม่ใช่แบบนั้นนะ ผมแค่พูดเล่นเพื่อให้บรรยากาศมันไม่เครียดน่ะ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
            new DialogueLine("เรย์นะ", "\"เรย์นะ: \"แต่มันทำให้ฉันเครียดกว่าเดิมค่ะ กลับไปเถอะ ฉันกลับเองได้\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    };

//==============================================================================================================================================================================    
    public static String getRaynaDay3Backgroud(){
        return "image\\_school_rooftop_2.jpg";
    };

    public static DialogueLine[] getRaynaDay3story(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"อีกนิดเดียว... อ๊ะ! (กล่องเกือบหล่นทับแต่คุณเข้าไปประคองไว้ได้ทันพอดี)\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                    new DialogueLine("พระเอก", "\"ระวังหน่อยครับประธาน! บอกแล้วไงว่าถ้าจะยกของสูงๆ ให้เรียกผม ไม่ต้องฝืนทำคนเดียวหรอกครับ\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                     new DialogueLine("เรย์นะ", "\"ฉัน... ฉันจัดการเองได้ค่ะ! ฉันไม่อยากให้คนอื่นมองว่าประธานนักเรียนอ่อนแอ... แค่กล่องใบเดียวฉันไม่... (เธอเงียบไปเพราะความเหนื่อยล้าที่สะสมมาหลายวัน)\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                };
        };

     public static String[] getRaynaDay3Choice(){
        return new String[] {"การขอความช่วยเหลือไม่ได้แปลว่าอ่อนแอนะครับ แต่มันแปลว่าเธอไว้ใจผมต่างหาก", "ถ้าตัวเล็กเกินจะหยิบถึงก็บอกผมสิครับ ไม่เห็นต้องทำเป็นเก่งเลย"};
    };

    public static DialogueLine[] getRaynaDay3ResponseA(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\" ไว้ใจ... งั้นเหรอคะ? คำพูดของนายนี่มัน... ฟังดูดีจนน่าหมั่นไส้จริงๆ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"ผมพูดจริงๆ นะครับ ผมน่ะ... อยากเป็นคนที่ประธานสามารถถอด 'หน้ากากประธาน' ออกแล้วเป็นแค่เรย์นะจังเวลาอยู่ด้วยกันแค่สองคนน่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("เรย์นะ", "\"(หน้าแดงก่ำจนถึงใบหู)\"ระ-เรย์นะจัง!? ใครอนุญาตให้เรียกชื่อต้นมิทราบคะ! แต่... วันนี้จะยอมให้ช่วยก็ได้... แค่วันนี้วันเดียวเท่านั้นนะ!\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
    };

     public static DialogueLine[] getRaynaDay3ResponseB(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"(ปัดมือคุณออกทันทีและพยายามยกกล่องเองจนเกือบชนชั้นวาง) \"ตัวเล็กงั้นเหรอ!? ฉันสูงตามมาตรฐานผู้หญิงญี่ปุ่นนะคะ! แล้วฉันก็ไม่ได้ทำเป็นเก่ง แต่มันคือความรับผิดชอบ!\"นั่นสินะคะ... ฉันลืมไปว่าในสายตานาย ฉันมันก็แค่ผู้หญิงที่แข็งแกร่งคนหนึ่ง\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"โธ่ ผมแค่เป็นห่วงนะ เดี๋ยวหน้าสวยๆ จะเสียโฉมหมด\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("เรย์นะ", "\"\"ถ้าห่วงก็ช่วยทำตัวให้มีประโยชน์มากกว่าการมาล้อเลียนส่วนสูงคนอื่นเถอะค่ะ! วันนี้ฉันจะตรวจงานคนเดียว เชิญนายกลับไปได้แล้ว!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    };

//==============================================================================================================================================================================

    public static String getRaynaDay4Backgroud(){
        return "image\\_school_in_spring_2.jpg";
    };

    public static DialogueLine[] getRaynaDay4story(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"ช่วงนี้... ฉันรู้สึกเหมือนมีคนแอบตามฉันน่ะ โดยเฉพาะตอนผ่านทางเดินมืดๆ... นายช่วยเดินไปส่งฉันที่สถานีรถไฟหน่อยได้ไหม?\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png"),
                    new DialogueLine("พระเอก", "\"ได้แน่นอนครับ ผมจะไม่ปล่อยให้ประธานเป็นอะไรไปเด็ดขาด\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
                };
        };

     public static String[] getRaynaDay4Choice(){
        return new String[] {"(กุมมือเธอเบาๆ) ไม่ต้องกลัวนะ ผมจะอยู่ข้างๆ เธอเอง ใครก็ทำอะไรเธอไม่ได้", "ประธานเป็นถึงนักกีฬาระดับโรงเรียนนี่นา สู้คนไหวอยู่แล้ว สู้ๆ นะ!"};
    };

    public static DialogueLine[] getRaynaDay4ResponseA(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\" (ไม่ปล่อยมือและกระชับให้แน่นขึ้น)\"นายเนี่ย... ชอบทำตัวเป็นบอดี้การ์ดอยู่เรื่อยเลยนะ แต่... ขอบคุณนะ ฉันอุ่นใจขึ้นเยอะเลยล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"ผมเต็มใจเป็นบอดี้การ์ดส่วนตัวให้เธอตลอดชีวิตเลยนะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("เรย์นะ", "\"พะ-พูดอะไรอายคนอื่นบ้างสิ! แต่... ก็สัญญาแล้วนะ ห้ามทิ้งกันล่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
    };

     public static DialogueLine[] getRaynaDay4ResponseB(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"(ถอนหายใจและปล่อยมือจากแขนเสื้อคุณ)\"นั่นสินะคะ... ฉันลืมไปว่าในสายตานาย ฉันมันก็แค่ผู้หญิงที่แข็งแกร่งคนหนึ่ง\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"อ้าว ผมชมอยู่นะเนี่ย ประธานเก่งจะตาย\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("เรย์นะ", "\"บางครั้งเก่งแค่ไหน ก็อยากมีคนมาคอยปกป้องบ้างนะคะ... ช่างเถอะ ฉันไปเองได้\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
    };

//==============================================================================================================================================================================
    public static String getRaynaDay5Backgroud(){
        return "image\\_school_ground_1.jpg";
    };

    public static DialogueLine[] getRaynaDay5story(){
        return new DialogueLine[] {
                    new DialogueLine("เคนจิ", "\"ไงจ๊ะ เรย์นะจัง? วันๆ มัวแต่ทำตัวเป็นคุณหนูอยู่ในกะลาเหรอ? พ่อฉันบอกว่าถ้าเธอไม่ยอมทำตามที่ฉันสั่ง... ตำแหน่งประธานของเธอคงอยู่ไม่ถึงพรุ่งนี้หรอกนะ!\"", "image\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
                    new DialogueLine("เรย์นะ", "\" (ตัวสั่นด้วยความโกรธและกลัว) \"กะ-แก... ออกไปจากห้องนี้เดี๋ยวนี้เคนจิ! ที่นี่ไม่ใช่ที่สำหรับคนอย่างแก!\"", "image\\22b9ada1-d037-49df-95c0-35e2c5531ded.png")
                };
        };

     public static String[] getRaynaDay5Choice(){
        return new String[] {"(ก้าวเข้าไปขวางหน้าเคนจิ)\"ออกไปจากห้องนี้ซะรุ่นพี่! ที่นี่ไม่ใช่สนามเด็กเล่นของพี่ และอย่ามาแตะต้องเรย์นะ!", "(เดินไปแจ้งอาจารย์)"};
    };

    public static DialogueLine[] getRaynaDay5ResponseA(){
        return new DialogueLine[] {
                    new DialogueLine("เคนจิ", "\"หึ! ไอ้หน้าอ่อนนี่เหรอที่จะปกป้องเธอ? ฝากไว้ก่อนเถอะ พรุ่งนี้ในงานเทศกาล... แกเตรียมตัวดูโชว์เด็ดได้เลย!\"", "image\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
                    new DialogueLine("พระเอก", "\"เป็นอะไรไหมเรย์นะ? ผมขอโทษที่ปล่อยให้เขาเข้ามาถึงที่นี่นะ\"", "ads"),
                    new DialogueLine("เรย์นะ", "\" (โผเข้ากอดคุณทั้งน้ำตา)\"ฮึก... ขอบคุณนะ... ฉันกลัวจริงๆ ว่าเขาจะทำลายทุกอย่าง... ขอบคุณที่อยู่ข้างฉันนะ\"", "image\\c4a1d014-6e7f-4c79-b295-13a803c1a711.png"),
                    new DialogueLine("พระเอก", "\"ผมจะไม่ยอมให้ใครมาทำลายสิ่งที่เธอรักเด็ดขาด เชื่อใจผมนะ\"", "ads"),
                };
    };

     public static DialogueLine[] getRaynaDay5ResponseB(){
        return new DialogueLine[] {
                    new DialogueLine("เคนจิ", "\"(หัวเราะร่าและเดินมาประชิดตัวเรย์นะ)\"ดูสิเรย์นะ! เพื่อนของเธอมันขี้ขลาดจนต้องวิ่งไปฟ้องครูเหมือนเด็กประถมเลยว่ะ ฮ่าๆๆ!\"", "image\\6cbe7b3b-9479-42fd-8b98-86dfdc6e7ae3.png"),
                    new DialogueLine("เรย์นะ", "\"(มองคุณที่เดินออกไปจากห้องด้วยสายตาที่แตกสลาย) \"นาย... ทิ้งฉันไว้ตรงนี้จริงๆ เหรอ...?\"", "ads"),
                    new DialogueLine("พระเอก", "\"ผมแค่ไปหาคนมาช่วยนะเรย์นะ อดทนหน่อย\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("เรย์นะ", "\"คนที่จะช่วยฉันควรจะเป็นนายสิ! ฮึก... ออกไปให้หมดเลยนะ!\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
        };
        };
    

//==============================================================================================================================================================================
    public static String getRaynaDay6Backgroud(){
        return "image\\_cultural_club_room_3.jpg";
    };

    public static DialogueLine[] getRaynaDay6story(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"ถ้าฉันไม่ใช่ประธานนักเรียนที่สมบูรณ์แบบ... นายยังจะอยากเคียงข้างฉันไหม? พรุ่งนี้... ฉันอาจจะเสียทุกอย่างไป\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
        };

     public static String[] getRaynaDay6Choice(){
        return new String[] {"ผมชอบเรย์นะที่เป็นเธอ ไม่ใช่ที่ตำแหน่งประธานนะ สัญญาเลยว่าจะไม่ไปไหน","ตำแหน่งประธานเหมาะกับเธอที่สุดแล้ว อย่าทิ้งมันไปง่ายๆ เลย"};
    };

    public static DialogueLine[] getRaynaDay6ResponseA(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"(น้ำตาคลอเบ้าและยิ้มอย่างอ่อนโยน)\"นั่นคือสิ่งที่ฉันอยากได้ยินมาตลอดเลยล่ะ... ขอบคุณนะที่มองเห็นตัวตนข้างในของฉัน\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png"),
                    new DialogueLine("พระเอก", "\"พรุ่งนี้สู้ไปด้วยกันนะ ผมจะอยู่ข้างๆ เธอเอง\"", "ads"),
                    new DialogueLine("เรย์นะ", "\"อื้ม! ฉันจะทำให้ดีที่สุดเพื่ออนาคตของเราสองคนค่ะ\"", "image\\Gemini_Generated_Image_e7ushxe7ushxe7us - แก้ไขแล้ว.png")
                };
    };

     public static DialogueLine[] getRaynaDay6ResponseB(){
        return new DialogueLine[] {
                    new DialogueLine("เรย์นะ", "\"สุดท้ายนายก็มองแค่หน้ากากที่ฉันใส่สินะ... ฉันมันก็แค่เครื่องมือสร้างชื่อเสียงให้โรงเรียนในสายตานาย\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png"),
                    new DialogueLine("พระเอก", "\"ไม่ใช่แบบนั้นนะ ผมแค่หวังดีกับอนาคตของเธอน่ะ\"", "ads"),
                    new DialogueLine("เรย์นะ", "\"อนาคตที่ไม่มีความสุขของฉันน่ะเหรอคะ? ขอบคุณนะ... แต่ฉันคิดว่าเรามองคนละมุมกันแล้วล่ะ\"", "image\\b90f8c5f-34e4-47bf-ae57-d2e6335b3d61.png")
    };
}

//===============================================================================================================================================================================
public static DialogueLine[] getReinaDateStory() {
        return new DialogueLine[] {
            new DialogueLine("เรย์นะ", "ไม่นึกเลยนะว่าคนอย่างนายจะกล้าชวนประธานนักเรียนมาที่คาเฟ่แบบนี้", "image\\Reina\\normal.png"),
            new DialogueLine("พระเอก", "ผมแค่อยากให้คุณได้พักผ่อนบ้างน่ะครับ เรย์นะ", "image\\Bgscene\\cafe_interior.jpg"),
            new DialogueLine("เรย์นะ", "(จิบชาเงียบๆ ก่อนจะหันมองหน้าต่าง) ...ขอบใจนะ นานๆ ทีได้อยู่เงียบๆ แบบนี้ก็ไม่เลวเหมือนกัน", "image\\Reina\\blushing.png"),
            new DialogueLine("ระบบ", "คุณและเรย์นะใช้เวลาช่วงเย็นด้วยกัน ความสัมพันธ์ของคุณทั้งคู่ดูสนิทสนมกันมากขึ้น", "")
        };
    }
 public static DialogueLine[] getReinaGiftStory() {
    return new DialogueLine[] {
        new DialogueLine("อาคาริ", "เอ๊ะ? ให้ฉันเหรอ? ว้าว! ขอบใจนะ! รู้ใจสมเป็นเพื่อนกันมานานจริงๆ","image\\Akari\\_blank.png"),
        new DialogueLine("อาคาริ", "เอ๊ะ? ให้ฉันเหรอ? ขอบใจนะ! ฉันจะรักษาอย่างดีเลยล่ะ!", "image\\Akari\\_blank.png")
    };
    }
}