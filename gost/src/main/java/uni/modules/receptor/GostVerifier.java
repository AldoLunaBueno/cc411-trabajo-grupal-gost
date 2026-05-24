package uni.modules.receptor;

import java.math.BigInteger;

import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

// Melissa
public class GostVerifier implements SignatureVerifier {

    @Override
    public boolean verify(Signature sig, BigInteger e, Point Q, DomainParameters params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verify'");
    }
    
}
