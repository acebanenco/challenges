package com.acebanenco.challenge.crypto.openssl;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.annotations.Out;
import jnr.ffi.types.size_t;

import java.awt.*;

public interface LibEvp {

    static LibEvp instance() {
        LibraryLoader<LibEvp> libraryLoader = LibraryLoader.create(LibEvp.class);
        // TODO libraryName
        return libraryLoader.load("libcrypto-3-x64");
    }

    /**
     * Allocates and returns a digest context.
     */
    Pointer EVP_MD_CTX_new();

    Pointer EVP_sha256();

    // const EVP_MD *EVP_get_digestbyname(const char *name);

    /**
     * Returns an EVP_MD structure when passed a digest name, a digest NID or an
     * ASN1_OBJECT structure respectively.
     * The EVP_get_digestbyname() function is present for backwards compatibility
     * with OpenSSL prior to version 3 and is different to the EVP_MD_fetch() function
     * since it does not attempt to "fetch" an implementation of the cipher.
     * Additionally, it only knows about digests that are built-in to OpenSSL and
     * have an associated NID. Similarly EVP_get_digestbynid() and EVP_get_digestbyobj()
     * also return objects without an associated implementation.
     * When the digest objects returned by these functions are used (such as in a call
     * to EVP_DigestInit_ex()) an implementation of the digest will be implicitly
     * fetched from the loaded providers. This fetch could fail if no suitable
     * implementation is available. Use EVP_MD_fetch() instead to explicitly fetch
     * the algorithm and an associated implementation from a provider.
     * See "ALGORITHM FETCHING" in crypto(7) for more information about fetching.
     * The digest objects returned from these functions do not need to be freed
     * with EVP_MD_free().
     */
    Pointer EVP_get_digestbyname(String name);

    // void EVP_MD_CTX_free(EVP_MD_CTX *ctx);

    /**
     * Cleans up digest context ctx and frees up the space allocated to it.
     */
    void EVP_MD_CTX_free(Pointer ctx);

    // int EVP_DigestInit_ex2(EVP_MD_CTX *ctx, const EVP_MD *type, const OSSL_PARAM params[])

    // int EVP_DigestInit_ex(EVP_MD_CTX *ctx, const EVP_MD *type, ENGINE *impl);
    /**
     * Sets up digest context ctx to use a digest type. type is typically supplied
     * by a function such as EVP_sha1(), or a value explicitly fetched with
     * EVP_MD_fetch().
     * If impl is non-NULL, its implementation of the digest type is used if there
     * is one, and if not, the default implementation is used.
     * The type parameter can be NULL if ctx has been already initialized with
     * another EVP_DigestInit_ex() call and has not been reset with EVP_MD_CTX_reset().
     */
    int EVP_DigestInit_ex(@Out Pointer ctx, Pointer type, Pointer impl);

    /**
     * Sets up digest context ctx to use a digest type. type is typically supplied
     * by a function such as EVP_sha1(), or a value explicitly fetched with EVP_MD_fetch().
     * The parameters params are set on the context after initialisation.
     * The type parameter can be NULL if ctx has been already initialized with
     * another EVP_DigestInit_ex() call and has not been reset with EVP_MD_CTX_reset().
     */
    int EVP_DigestInit_ex2(Pointer ctx, Pointer type, Pointer params);

    // int EVP_DigestUpdate(EVP_MD_CTX *ctx, const void *d, size_t cnt);

    /**
     * Hashes cnt bytes of data at d into the digest context ctx. This function
     * can be called several times on the same ctx to hash additional data.
     */
    int EVP_DigestUpdate(Pointer ctx, Pointer data, @size_t long cnt);

    // int EVP_DigestFinal_ex(EVP_MD_CTX *ctx, unsigned char *md, unsigned int *s);
    /*
     * Retrieves the digest value from ctx and places it in md. If the s parameter
     * is not NULL then the number of bytes of data written (i.e. the length of
     * the digest) will be written to the integer at s, at most EVP_MAX_MD_SIZE
     * bytes will be written. After calling EVP_DigestFinal_ex() no additional
     * calls to EVP_DigestUpdate() can be made, but EVP_DigestInit_ex2() can be
     * called to initialize a new digest operation.
     */
    int EVP_DigestFinal_ex(Pointer ctx, @Out Pointer md, Pointer s);

}
