package agoption.projetos.com.agoption.helper;

public class InterpretaTexto {

    private String texto;

    public InterpretaTexto(String texto){
        this.texto = texto;
    }

    public String RetornaValor(String opcao){
        String[] palavras = this.texto.split(" ");
        String plAnterior, plAtual, textoFinal;
        boolean bPossuiEtanol =  false, bPossuiGasolina = false, bENumero = false;

        textoFinal = "";
        plAtual = "";

         switch (opcao.toUpperCase()) {
             case "ETANOL":
                 for(int i = 0; i < palavras.length; i++){

                     if(palavras[i].toUpperCase().equals("ALCOOL") || palavras[i].toUpperCase().equals("ETANOL") ){
                         bPossuiEtanol = true;
                         //Testa as próximas três posições
                         /*===========================
                                POSSIBILIDADES
                           ===========================
                            R$ 4,25
                            4,25
                            4:25
                            4 e 25
                           ===========================*/
                         try{
                             //PRÓXIMA ATUAL
                             if (palavras[i+1].toUpperCase().equals("R$")){
                                 i += 2;
                                 plAtual = String.valueOf(Double.parseDouble(palavras[i].replace(":",".").replace(",",".")));
                             }else{
                                 i += 1;
                                 plAtual = String.valueOf(Double.parseDouble(palavras[i].replace(":",".").replace(",",".")));
                             }

                             //PRÓXIMA PALAVRA
                             if(i + 1 < palavras.length) {
                                if (palavras[i+1].toUpperCase() == "E"){
                                 plAtual = plAtual + "." + String.valueOf(Double.parseDouble(palavras[i+2].replace(":",".").replace(",",".")));
                                }
                             }

                             bENumero = true;

                         }catch(NumberFormatException e ){
                             bENumero = false;
                         }

                         if(bENumero == true){
                             textoFinal =  plAtual;
                         }
                     }
                 }
                 break;

             case "GASOLINA":
                 for(int index = 0; index < palavras.length; index++){

                     if(palavras[index].toUpperCase().equals("GASOLINA")){
                         bPossuiGasolina = true;

                         //Testa as próximas três posições
                         /*===========================
                                POSSIBILIDADES
                           ===========================
                            R$ 4,25
                            4,25
                            4:25
                            4 e 25
                           ===========================*/
                         try{
                             //PRÓXIMA ATUAL
                             if (palavras[index+1].toUpperCase().equals("R$")){
                                 index = index + 2;
                                 plAtual = String.valueOf(Double.parseDouble(palavras[index].replace(":",".").replace(",",".")));
                             }else{
                                 index = index + 1;
                                 plAtual = String.valueOf(Double.parseDouble(palavras[index].replace(":",".").replace(",",".")));
                             }

                             //PRÓXIMA PALAVRA
                             if((index + 1) < palavras.length) {
                                 if (palavras[index+1].toUpperCase() == "E" ){
                                     plAtual = plAtual + "." + String.valueOf(Double.parseDouble(palavras[index+2].replace(":",".").replace(",",".")));
                                 }
                             }

                             bENumero = true;

                         }catch(NumberFormatException e ){
                             bENumero = false;
                         }

                         if(bENumero == true){
                             textoFinal =  plAtual;
                         }
                     }
                 }
                 break;
         }

        return textoFinal;
    }


}
