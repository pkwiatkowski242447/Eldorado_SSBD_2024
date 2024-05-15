import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useAccount} from "@/hooks/useAccount.ts";

const formSchema = z.object({
    login: z.string().min(2, {message: "Login has to be at least 2 characters long."})
        .max(32, {message: "Login has to be at most 32 characters long."}), //TODO add regex for login
    password: z.string().min(2, {message: "Password has to be at least 2 characters long."})
        .max(60, {message: "Password has to be at most 60 characters long."})
})

function LoginForm() {

    const {logIn} = useAccount();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            login: "",
            password: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        logIn(values.login, values.password).catch((error) => {
            console.log(error.response.data)
        });
    }

    return (
        <Card className="mx-auto max-w-sm">
            <CardHeader>
                <CardTitle className="text-2xl">Login</CardTitle>
                <CardDescription>
                    Enter your login info below to proceed
                </CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                        <FormField
                            control={form.control}
                            name="login"
                            render={({field}) => (
                                <FormItem>
                                    <div className="grid gap-4">
                                        <FormLabel className="text-left">Login</FormLabel>
                                        <FormControl>
                                            <Input placeholder="" {...field} />
                                        </FormControl>
                                        <FormMessage/>
                                    </div>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="password"
                            render={({field}) => (
                                <FormItem>
                                    <div className="grid gap-4">
                                        <div className="flex justify-between items-center">
                                            <FormLabel className="text-left">Password</FormLabel>
                                            <a href="/forgot-password"
                                               className="inline-block text-black underline">
                                                Forgot your password?
                                            </a>
                                        </div>
                                        <FormControl>
                                            <Input type="password" {...field} />
                                        </FormControl>
                                        <FormMessage/>
                                    </div>
                                </FormItem>
                            )}
                        />
                        <Button type="submit" className="w-full">Submit</Button>
                        <div className="mt-4 text-center text-sm">
                            Don&apos;t have an account?{" "}
                            <a href="/register" className="font-medium text-black hover:text-blue-500">
                                Sign up </a>
                        </div>
                    </form>
                </Form>
            </CardContent>
        </Card>
    )
}

export default LoginForm
