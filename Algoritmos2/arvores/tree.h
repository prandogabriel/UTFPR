typedef struct node Node;


Node * create(int key);
int release(Node *no);
Node * search(Node *tree, int value);
Node * insert(Node *tree, int value);
int delete(Node *tree, int value);
void prefix(Node *tree);
void posfix(Node *tree);
void node_leaves(Node *tree);
