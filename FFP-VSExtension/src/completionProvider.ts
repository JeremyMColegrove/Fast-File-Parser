import * as vscode from 'vscode';

export class CompletionProvider implements vscode.CompletionItemProvider {

    // Provide completion items
    public provideCompletionItems(
        document: vscode.TextDocument,
        position: vscode.Position,
        token: vscode.CancellationToken,
        context: vscode.CompletionContext
    ): vscode.CompletionItem[] | Thenable<vscode.CompletionItem[]> {
        const lineText = document.lineAt(position.line).text;
        const currentWord = this.getCurrentWord(lineText, position.character);

        // Define your completion items based on context
        const completionItems: vscode.CompletionItem[] = [];
        // Add completion items based on the context
        if (lineText.includes('IF')) {
            completionItems.push(this.createCompletionItem('THEN', 'Keyword indicating the next part of the conditional statement'));
        }
        
        if (lineText.includes('FOR')) {
            completionItems.push(this.createCompletionItem('DO', 'Keyword indicating the start of the loop body'));
        }

        if (lineText.includes('SET')) {
            completionItems.push(this.createCompletionItem('TO', 'Keyword to assign a value'));
        }

        // You can also provide contextual hints based on what the user is currently typing
        if (currentWord === 'var') {
            completionItems.push(this.createCompletionItem('myVariable', 'A sample variable'));
        }

        return completionItems;
    }

    // Helper function to create a completion item
    private createCompletionItem(label: string, detail: string): vscode.CompletionItem {
        const completionItem = new vscode.CompletionItem(label, vscode.CompletionItemKind.Keyword);
        completionItem.detail = detail;
        return completionItem;
    }

    // Helper function to extract the current word being typed
    private getCurrentWord(lineText: string, character: number): string {
        const textBeforeCursor = lineText.substring(0, character);
        const match = textBeforeCursor.match(/(\w+)$/);
        return match ? match[1] : '';
    }
}
