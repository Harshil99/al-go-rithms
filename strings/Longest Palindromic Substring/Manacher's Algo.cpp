char text[100]; 
int min(int a, int b) 
{ 
    int res = a; 
    if(b < a) 
        res = b; 
    return res; 
} 
  
void findLongestPalindromicString() 
{ 
    int N = strlen(text); 
    if(N == 0) 
        return; 
    N = 2*N + 1;
    int L[N]; 
    L[0] = 0; 
    L[1] = 1; 
    int C = 1;
    int R = 2;  
    int i = 0; 
    int iMirror;
    int maxLPSLength = 0; 
    int maxLPSCenterPosition = 0; 
    int start = -1; 
    int end = -1; 
    int diff = -1; 
      
    for (i = 2; i < N; i++)  
    { 
        iMirror  = 2*C-i; 
        L[i] = 0; 
        diff = R - i; 
        
        if(diff > 0) 
            L[i] = min(L[iMirror], diff); 

        while ( ((i + L[i]) < N && (i - L[i]) > 0) &&  
            ( ((i + L[i] + 1) % 2 == 0) ||  
            (text[(i + L[i] + 1)/2] == text[(i - L[i] - 1)/2] ))) 
        { 
            L[i]++; 
        } 
  
        if(L[i] > maxLPSLength)
        { 
            maxLPSLength = L[i]; 
            maxLPSCenterPosition = i; 
        } 

        if (i + L[i] > R)  
        { 
            C = i; 
            R = i + L[i]; 
		}
    } 
    
    start = (maxLPSCenterPosition - maxLPSLength)/2; 
    end = start + maxLPSLength - 1; 
    printf("LPS of string is %s : ", text); 
    for(i=start; i<=end; i++) 
        printf("%c", text[i]); 
    printf("\n"); 
}
