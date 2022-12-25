package tkachgeek.tkachutils.text;

public enum DeclinationsType {
  nominative("именительный"), genitive("родительный"), dative("дательный"), accusative("винительный"), instrumental("творительный"), prepositional("предложный"),
  multiple_nominative("множественный именительный"), multiple_genitive("множественный родительный"), multiple_dative("множественный дательный"), multiple_accusative("множественный винительный"), multiple_instrumental("множественный творительный"), multiple_prepositional("множественный предложный");
  
  DeclinationsType(String russian) {
  }
}
