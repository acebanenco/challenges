package com.acebanenco.challenge.crypto.openssl;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.annotations.Out;
import jnr.ffi.types.size_t;

public interface LibSha256 {

    static LibSha256 instance() {
        LibraryLoader<LibSha256> libraryLoader = LibraryLoader.create(LibSha256.class);
        // TODO libraryName
        return libraryLoader.load("libcrypto-3-x64");
    }

    // int SHA256_Init(SHA256_CTX *c);
    int SHA256_Init(@Out Sha256StateStruct context);

    // int SHA256_Update(SHA256_CTX *c, const void *data, size_t len);
    int SHA256_Update(Sha256StateStruct context, Pointer data, @size_t long len);

    // int SHA256_Final(unsigned char *md, SHA256_CTX *c);
    int SHA256_Final(Pointer md, Sha256StateStruct context);

    // void SHA256_Transform(SHA256_CTX *c, const unsigned char *data);
    void SHA256_Transform(Sha256StateStruct context, Pointer data);

    // char *SHA224(const unsigned char *d, size_t n, unsigned char *md);
    Pointer SHA256(Pointer data, @size_t long length, Pointer digest);

    //    define SHA256_DIGEST_LENGTH    32
    //    define SHA_LONG unsigned int
    //    define SHA_LBLOCK      16
    class Sha256StateStruct extends Struct {
        //        SHA_LONG h[8];
        public Unsigned32[] h;
        //        SHA_LONG Nl, Nh;
        public Unsigned32 Nl;
        public Unsigned32 Nh;
        //        SHA_LONG data[SHA_LBLOCK];
        public Unsigned32[] data;
        //        unsigned int num, md_len;
        public Unsigned32 num;
        public Unsigned32 md_len;

        public Sha256StateStruct(Runtime runtime) {
            super(runtime);
            h = array(new Unsigned32[8]);
            Nl = new Unsigned32();
            Nh = new Unsigned32();
            data = array(new Unsigned32[16]);
            num = new Unsigned32();
            md_len = new Unsigned32();
        }
    }
}
